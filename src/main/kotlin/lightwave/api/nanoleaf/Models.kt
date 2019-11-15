package lightwave.api.nanoleaf


class Power(val on: BoolValue) {
  constructor(on: Boolean) : this(BoolValue(on))
}

class Brightness(val brighness: DurationValue) {
  constructor(newValue: Int) : this(DurationValue(newValue))
}

data class BoolValue(val value: Boolean)

data class RangeValue(
  val value: Int,
  val max: Int,
  val min: Int
)

data class DurationValue(val value: Int, val duration: Int = 4)

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
        o[i++] = v.id.toByte()
        o[i++] = 1.toByte()
        o[i++] = v.color.r.toByte()
        o[i++] = v.color.g.toByte()
        o[i++] = v.color.b.toByte()
        o[i++] = 0.toByte()
        o[i++] = 5.toByte() // 1/10 of a second , so half second
      }
    }
}

class PanelConfig(val id: Int, val color: PanelColor)

data class PanelColor(val r: Int, val g: Int, val b: Int)