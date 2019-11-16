package lightwave.api.nanoleaf.models

import lightwave.api.nanoleaf.rendering.PanelConfig

class PanelSet(p: List<PanelConfig>) {
  private val panels = p.map { it.id to it }.toMap()

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
      for ((k, v) in panels) {
//        println("${v.id}: (${v.color.r}, ${v.color.g}, ${v.color.b})")

        o[i++] = v.id.toByte()
        o[i++] = 1.toByte()
        o[i++] = v.color.r.toByte()
        o[i++] = v.color.g.toByte()
        o[i++] = v.color.b.toByte()
        o[i++] = 0.toByte()
        o[i++] = 0.toByte() // 1/10 of a second
      }
    }
}