package lightwave.api.nanoleaf.rendering.effects

import lightwave.api.nanoleaf.models.Panel
import lightwave.api.nanoleaf.models.PanelSet
import lightwave.api.nanoleaf.rendering.Paxel
import lightwave.api.nanoleaf.rendering.colors.ColorRGB
import lightwave.api.nanoleaf.rendering.colors.Blend

class FadeEffect(
  layout: Panel,
  from: ColorRGB = ColorRGB(255, 0, 0),
  to: ColorRGB = ColorRGB(0, 255, 0)
) : Effect(layout) {

  private val blend = Blend(from.lab(), to.lab())

  override fun frame(progress: Double): PanelSet {
    return PanelSet(layout.panelIdsByX.map { Paxel(it, blend.to(progress)) })
  }
}