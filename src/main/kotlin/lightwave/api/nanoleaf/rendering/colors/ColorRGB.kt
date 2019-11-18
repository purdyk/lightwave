package lightwave.api.nanoleaf.rendering.colors

import lightwave.api.nanoleaf.rendering.colors.Color.Companion.CONSTANT_X
import lightwave.api.nanoleaf.rendering.colors.Color.Companion.CONSTANT_Y
import lightwave.api.nanoleaf.rendering.colors.Color.Companion.CONSTANT_Z
import lightwave.api.nanoleaf.rendering.colors.Color.Companion.rgbXyz
import lightwave.api.nanoleaf.rendering.colors.Color.Companion.xyzLab

data class ColorRGB(val r: Double, val g: Double, val b: Double, val alpha: Double = 1.0) {

  constructor(iR: Int, iG: Int, iB: Int) :
      this(
        (iR / 255.0).coerceIn(0.0, 1.0),
        (iG / 255.0).coerceIn(0.0, 1.0),
        (iB / 255.0).coerceIn(0.0, 1.0)
      )

  fun lab(): ColorLAB {
    val nR = rgbXyz(this.r * 255)
    val nG = rgbXyz(this.g * 255)
    val nB = rgbXyz(this.b * 255)
    val x =
      xyzLab(
        (0.4124564 * nR + 0.3575761 * nG + 0.1804375 * nB)
            / CONSTANT_X
      )
    val y =
      xyzLab(
        (0.2126729 * nR + 0.7151522 * nG + 0.0721750 * nB)
            / CONSTANT_Y
      )
    val z =
      xyzLab(
        (0.0193339 * nR + 0.1191920 * nG + 0.9503041 * nB)
            / CONSTANT_Z
      )
    return ColorLAB(
      (116.0 * y - 16.0),
      (500.0 * (x - y)),
      (200.0 * (y - z)),
      alpha
    )
  }

  fun blend(other: ColorRGB): ColorRGB {
    return ColorRGB(
      (this.r + ((other.r - this.r) * other.alpha)),
      (this.g + ((other.g - this.g) * other.alpha)),
      (this.b + ((other.b - this.b) * other.alpha)),
      1.0
    )
  }
}