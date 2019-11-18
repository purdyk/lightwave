package lightwave.api.nanoleaf.models

import lightwave.api.nanoleaf.rendering.Paxel
import kotlin.math.roundToInt

class PanelSet(private val panels: MutableMap<Int, Paxel>)
  : MutableMap<Int, Paxel> by panels {

  constructor(p: List<Paxel>) : this(p.map { it.id to it }.toMap().toMutableMap())

  fun put(cfg: Paxel) {
    put(cfg.id, cfg)
  }

  fun count(): Int {
    return panels.count()
  }

  fun diff(from: PanelSet?): PanelSet =
    PanelSet(panels.filter { it.value.color != from?.panels?.get(it.key)?.color }.map { it.value })

  fun toByteArray(): ByteArray =
    ByteArray(panels.size * 7 + 1).also { o ->
      var i = 0;

      o[i++] = panels.size.toByte()

      // PanelID, Frame Count, per-frame: R,G,B,W,T
      for ((_, v) in panels) {
//        println("${v.id}: (${v.color.r}, ${v.color.g}, ${v.color.b})")

        val c = v.color.rgb()

        o[i++] = v.id.toByte()
        o[i++] = 1.toByte()
        o[i++] = (c.r * 255).roundToInt().toByte()
        o[i++] = (c.g * 255).roundToInt().toByte()
        o[i++] = (c.b * 255).roundToInt().toByte()
        o[i++] = 0.toByte()
        o[i++] = 0.toByte() // 1/10 of a second
      }
    }
}