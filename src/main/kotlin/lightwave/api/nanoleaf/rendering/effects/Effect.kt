package lightwave.api.nanoleaf.rendering.effects

import lightwave.api.nanoleaf.models.Panel
import lightwave.api.nanoleaf.models.PanelSet

abstract class Effect(protected val layout: Panel) {
  abstract fun frame(progress: Double): PanelSet
}