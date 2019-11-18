package lightwave.api.nanoleaf.rendering.colors

import kotlin.math.pow
import kotlin.math.roundToInt

internal class Color {

  companion object {
    internal const val CONSTANT_K = 18.0
    internal const val CONSTANT_X = 0.950470
    internal const val CONSTANT_Y = 1.0
    internal const val CONSTANT_Z = 1.088830

    internal fun rgbXyz(r: Double): Double {
      var r = r
      return if (255.0.let { r /= it; r } <= 0.04045) {
        r / 12.92
      } else {
        ((r + 0.055) / 1.055).pow(2.4)
      }
    }

    internal fun xyzRgb(r: Double): Double {
      return (255.0 *
          if (r <= 0.00304) 12.92 * r
          else 1.055 * r.pow(1 / 2.4) - 0.055
          ).roundToInt().toDouble()
    }

    internal fun xyzLab(x: Double): Double {
      return if (x > 0.008856) {
        x.pow(1.0 / 3.0)
      } else {
        7.787037 * x + 4.0 / 29.0
      }
    }

    internal fun labXyz(x: Double): Double {
      return if (x > 0.206893034) {
        x * x * x
      } else {
        (x - 4.0f / 29.0) / 7.787037
      }
    }

  }
}