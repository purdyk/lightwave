package lightwave.api.nanoleaf.rendering.effects

import lightwave.api.nanoleaf.models.Panel
import lightwave.api.nanoleaf.models.PanelSet
import lightwave.api.nanoleaf.rendering.Paxel
import lightwave.api.nanoleaf.rendering.colors.ColorRGB
import lightwave.api.nanoleaf.rendering.colors.Blend
import kotlin.math.floor


// Similar to point effect
// Displays 2 paxels:
//   most recently completed with "on" at 1.0 alpha
//   and "current" with "on" at a percentage alpha.
//  ex: given 4.5, the 5th paxel will be full on and the 6th will be at 50%

class ProgressEffect(
  layout: Panel, direction: Direction,
  on: ColorRGB = ColorRGB(
    70,
    70,
    227
  )
) : LinearEffect(layout, direction) {

  private val on = on.lab()

  // Progress is between 1 and 0
  override fun frame(progress: Double): PanelSet {
    val perPanelProgress = 1.0 / orderIds.size

    // example 0.5 and 9 panels, we get fract = 4.5, fp = 4, last = 0.5
    val fract = progress / perPanelProgress
    val fp = floor(fract).toInt()
    val last = fract - fp

    return PanelSet(listOf(
      orderIds.getOrNull(fp - 1)?.let { Paxel(it, on) },
      orderIds.getOrNull(fp)?.let { Paxel(it, on.copy(alpha = last)) }
    ).mapNotNull { it })

  }
}
