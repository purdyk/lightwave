package lightwave.api.nanoleaf.rendering

import lightwave.api.nanoleaf.models.PanelSet

class FrameLooper(
  val gen: LinearFrameGenerator,
  val times: Int = 10, val steps: Int = 100, private val delay: Long = 25,
  val perFrame: (PanelSet) -> Unit
) {

  private val fract = steps.toDouble()

  fun loop() {
    for (x in 0 until times) {
      for (i in 0..steps) {
        perFrame(gen.frame(i / fract))
        Thread.sleep(delay)
      }
      for (i in steps downTo 0) {
        perFrame(gen.frame(i / fract))
        Thread.sleep(delay)
      }
    }
  }

}