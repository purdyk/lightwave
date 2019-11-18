package lightwave.api.nanoleaf.rendering.effects

import lightwave.api.nanoleaf.models.Panel
import lightwave.api.nanoleaf.models.PanelSet
import lightwave.api.nanoleaf.rendering.Paxel
import lightwave.api.nanoleaf.rendering.colors.Blend
import lightwave.api.nanoleaf.rendering.colors.ColorRGB

// Produces a blended "progress" bar
// that proceeds across 2 colors and
// stretches out the palette

class BlendEffect(
  layout: Panel, direction: Direction,
  private val from: ColorRGB = ColorRGB(255,0,0),
  private val to: ColorRGB = ColorRGB(0,0,255)
) : LinearEffect(layout, direction) {

  private val blend = Blend(from.lab(), to.lab())

  override fun frame(progress: Double): PanelSet {
    // 0.5 and 9 panels
    // == 4.5 overall progress
    val overall = orderIds.size * progress

    // each panel is somewhere inside the overall span
    // consider each panel's midpoint
    // 0-1 1-2 2-3: panel index 0 is 0.5

    // map overall progress back to range of 0 to 1

    return PanelSet(orderIds.mapIndexedNotNull { idx, id ->
      val mid = idx + 0.5
      if (mid <= overall) {
        Paxel(id, blend.to(mid / overall))
      } else null
    })

  }

}