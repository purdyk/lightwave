package lightwave.api.nanoleaf.rendering.effects

import lightwave.api.nanoleaf.models.Panel
import lightwave.api.nanoleaf.models.PanelSet
import lightwave.api.nanoleaf.rendering.Paxel
import lightwave.api.nanoleaf.rendering.colors.ColorRGB
import lightwave.api.nanoleaf.rendering.colors.Blend
import kotlin.math.ceil
import kotlin.math.floor


class PointEffect(
  layout: Panel, direction: Direction,
  on: ColorRGB = ColorRGB(70, 70,227)
) : LinearEffect(layout, direction) {

  private val on = on.lab()

  // Progress is between 1 and 0
  override fun frame(progress: Double): PanelSet {
    val perPanelProgress = 1.0 / (orderIds.size - 1)

    // example 0.5 and 9 panels, we get fract = 4.5, fp = 4, pct = 0.5
    // panel 4 should be half way, and panel 0.5 should be half way
    val fract = progress / perPanelProgress

    val fp = floor(fract).toInt()
    val sp = ceil(fract).toInt()

    val fPct = sp - fract
    val sPct = fract - fp

    return PanelSet(listOf(
      Paxel(orderIds[fp], on.copy(alpha = fPct)),
      Paxel(orderIds[sp], on.copy(alpha = sPct))
    ))

  }
}
