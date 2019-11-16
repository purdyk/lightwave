package lightwave.api.nanoleaf.rendering

import kotlin.math.pow
import kotlin.math.roundToInt

class PanelConfig(val id: Int, val color: ColorRGB) {
  class ColorRGB(iR: Int, iG: Int, iB: Int) {

    val r = iR.coerceAtLeast(0).coerceAtMost(255)
    val g = iG.coerceAtLeast(0).coerceAtMost(255)
    val b = iB.coerceAtLeast(0).coerceAtMost(255)

    fun lab(): ColorLAB {
      val nR = rgbXyz(this.r.toDouble())
      val nG = rgbXyz(this.g.toDouble())
      val nB = rgbXyz(this.b.toDouble())
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
        (116.0 * y - 16.0).roundToInt(),
        (500.0 * (x - y)).roundToInt(),
        (200.0 * (y - z)).roundToInt()
      )
    }
  }

  data class ColorLAB(val l: Int, val a: Int, val b: Int) {
    fun rgb(): ColorRGB {
      var y: Double = (this.l + 16.0) / 116.0
      var x: Double = y + this.a / 500.0
      var z: Double = y - this.b / 200.0
      x = labXyz(x) * CONSTANT_X
      y = labXyz(y) * CONSTANT_Y
      z = labXyz(z) * CONSTANT_Z
      val r = xyzRgb(
        3.2404542 * x - 1.5371385 * y - 0.4985314 * z
      ).roundToInt()
      val g = xyzRgb(
        -0.9692660 * x + 1.8760108 * y + 0.0415560 * z
      ).roundToInt()
      val b = xyzRgb(
        0.0556434 * x - 0.2040259 * y
            + 1.0572252 * z
      ).roundToInt()
      return ColorRGB(r, g, b)
    }
  }

  class Scale(private val a: ColorLAB, private val b: ColorLAB) {

    fun to(pct: Double): ColorLAB {
      return ColorLAB(
        (a.l + ((b.l - a.l) * pct)).roundToInt(),
        (a.a + ((b.a - a.a) * pct)).roundToInt(),
        (a.b + ((b.b - a.b) * pct)).roundToInt()
      )
    }

  }

  companion object {
    private const val CONSTANT_K = 18.0
    private const val CONSTANT_X = 0.950470
    private const val CONSTANT_Y = 1.0
    private const val CONSTANT_Z = 1.088830

    private fun rgbXyz(r: Double): Double {
      var r = r
      return if (255.0.let { r /= it; r } <= 0.04045) {
        r / 12.92
      } else {
        ((r + 0.055) / 1.055).pow(2.4)
      }
    }

    private fun xyzRgb(r: Double): Double {
      return (255.0 *
          if (r <= 0.00304) 12.92 * r
          else 1.055 * r.pow(1 / 2.4) - 0.055
          ).roundToInt().toDouble()
    }

    private fun xyzLab(x: Double): Double {
      return if (x > 0.008856) {
        x.pow(1.0 / 3.0)
      } else {
        7.787037 * x + 4.0 / 29.0
      }
    }

    private fun labXyz(x: Double): Double {
      return if (x > 0.206893034) {
        x * x * x
      } else {
        (x - 4.0f / 29.0) / 7.787037
      }
    }

  }
}