package lightwave.api.nanoleaf.rendering.animation

import lightwave.api.nanoleaf.NanoleafStream
import lightwave.api.nanoleaf.models.PanelSet
import lightwave.api.nanoleaf.rendering.Paxel
import lightwave.api.nanoleaf.rendering.effects.Effect
import java.lang.Thread.sleep

class Animator(private val delay: Long) : Serial() {

  fun into(stream: NanoleafStream) {
    while (true) {
      val time = System.currentTimeMillis()
      if (wouldComplete(time))
        break

      stream.push(frame(time))
      sleep(delay)
    }
  }

  companion object {
    fun animate(delay: Long, init: Animator.() -> Unit): Animator {
      val animator = Animator(delay)
      animator.init()
      return animator
    }
  }

}

interface Animation {
  fun wouldComplete(time: Long): Boolean
  fun frame(time: Long): PanelSet
}

open class AnimationGenerator {
  protected val animations = ArrayList<Animation>()

  fun inSequence(init: AnimationGenerator.() -> Unit) {
    val serial = Serial()
    serial.init()
    animations.add(serial)
  }

  fun together(init: AnimationGenerator.() -> Unit) {
    val parallel = Parallel()
    parallel.init()
    animations.add(parallel)
  }

  fun through(duration: Double, init: AnimationGenerator.() -> Effect) {
    animations.add(Timed(duration, this.init()))
  }

  fun always(init: AnimationGenerator.() -> Effect) {
    animations.add(Always(this.init()))
  }

//  fun looped(init: AnimationGenerator.() -> Unit) {
//    val looped = Looped()
//    looped.init()
//    animations.add(looped)
//  }

}

open class Serial : AnimationGenerator(), Animation {
  var current = 0

  override fun wouldComplete(time: Long): Boolean {
    while (current < animations.size) {
      if (!animations[current].wouldComplete(time))
        return false
      current++
    }
    return true
  }

  override fun frame(time: Long): PanelSet
      = animations[current].frame(time)
}
private class Parallel : AnimationGenerator(), Animation {
  override fun wouldComplete(time: Long): Boolean
      = animations.filter { it !is Always }.all { it.wouldComplete(time) }

  override fun frame(time: Long): PanelSet {
    val set = PanelSet(emptyList())
    for (each in animations) {
      val newSet = each.frame(time)

      // Alpha blend the new set onto the old one
      newSet.forEach { (id, newP) ->
        set[id]?.also { oldP ->
          set[id] = Paxel(id, oldP.color.blend(newP.color))
        } ?: run {
          set[id] = newP
        }
      }
    }
    return set
  }
}

//private class Looped: AnimationGenerator(), Animation {



//}

private class Always(private val effect: Effect): Animation {
  override fun wouldComplete(time: Long): Boolean = false

  override fun frame(time: Long): PanelSet {
    return effect.frame(0.5)
  }
}

private class Timed(duration: Double, private val effect: Effect) : Animation {
  private val duration: Long = (duration * 1000).toLong()

  private var start: Long? = null

  override fun wouldComplete(time: Long): Boolean {
    return time - (start ?: time) > duration
  }

  override fun frame(time: Long): PanelSet {
    if (start == null) {
      start = time
      println("Starting Timed: ${effect::class.java.simpleName}")
    }

    val frameTime = time - (start ?: time)

    val progress = (frameTime % duration).toDouble() / duration

    return effect.frame(progress)
  }
}