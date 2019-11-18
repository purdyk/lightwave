package lightwave.api.nanoleaf.rendering.effects

import lightwave.api.nanoleaf.models.Panel
import lightwave.api.nanoleaf.models.PanelSet
import lightwave.api.nanoleaf.rendering.Paxel
import lightwave.api.nanoleaf.rendering.colors.ColorRGB

class FillEffect(
  layout: Panel,
  fill: ColorRGB = ColorRGB(0, 0, 0)
) : Effect(layout) {

  private val fill = fill.lab()

  override fun frame(progress: Double): PanelSet {
    return PanelSet(layout.panelIdsByX.map { Paxel(it, fill) })
  }
}