package lightwave.api.nanoleaf.rendering.colors

import lightwave.api.nanoleaf.rendering.colors.Color.Companion.CONSTANT_X
import lightwave.api.nanoleaf.rendering.colors.Color.Companion.CONSTANT_Y
import lightwave.api.nanoleaf.rendering.colors.Color.Companion.CONSTANT_Z
import lightwave.api.nanoleaf.rendering.colors.Color.Companion.labXyz
import lightwave.api.nanoleaf.rendering.colors.Color.Companion.xyzRgb

class ColorLAB(iL: Double, iA: Double, iB: Double, val alpha: Double) {

  val l = iL.coerceIn(0.0, 255.0)
  val a = iA
  val b = iB

  fun rgb(): ColorRGB {
    var y = (this.l + 16.0) / 116.0
    var x = y + this.a / 500.0
    var z = y - this.b / 200.0
    x = labXyz(x) * CONSTANT_X
    y = labXyz(y) * CONSTANT_Y
    z = labXyz(z) * CONSTANT_Z
    val r = xyzRgb(
      3.2404542 * x - 1.5371385 * y - 0.4985314 * z
    )
    val g = xyzRgb(
      -0.9692660 * x + 1.8760108 * y + 0.0415560 * z
    )
    val b = xyzRgb(
      0.0556434 * x - 0.2040259 * y
          + 1.0572252 * z
    )
    return ColorRGB(r / 255, g / 255, b / 255, alpha)
  }

  // This needs to be done in RGB color space
  fun blend(other: ColorLAB): ColorLAB
      = this.rgb().blend(other.rgb()).lab()

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ColorLAB

    if (alpha != other.alpha) return false
    if (l != other.l) return false
    if (a != other.a) return false
    if (b != other.b) return false

    return true
  }

  override fun hashCode(): Int {
    var result = alpha.hashCode()
    result = 31 * result + l.hashCode()
    result = 31 * result + a.hashCode()
    result = 31 * result + b.hashCode()
    return result
  }

  fun copy(alpha: Double): ColorLAB {
    return ColorLAB(l, a, b, alpha)
  }

}