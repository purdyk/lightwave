package lightwave.api.nanoleaf.rendering

import lightwave.api.nanoleaf.models.Panel
import lightwave.api.nanoleaf.models.PanelSet
import kotlin.math.floor


class ProgressFrameGenator(
  layout: Panel, direction: Direction,
  private val off: PanelConfig.ColorRGB = PanelConfig.ColorRGB(250, 120, 20),
  private val on: PanelConfig.ColorRGB = PanelConfig.ColorRGB(70, 70, 227)
) : LinearFrameGenerator(layout, direction) {

  private val scale = PanelConfig.Scale(off.lab(), on.lab())

  // Progress is between 1 and 0
  override fun frame(progress: Double): PanelSet {
    val perPanelProgress = 1.0 / orderIds.size

    // example 0.5 and 9 panels, we get fract = 4.5, fp = 4, last = 0.5
    val fract = progress / perPanelProgress
    val fp = floor(fract).toInt()
    val last = fract - fp

    return orderIds.mapIndexed { idx, e ->
      when {
        idx < fp -> PanelConfig(e, on)
        idx > fp -> PanelConfig(e, off)
        else -> PanelConfig(e, scale.to(last).rgb())
      }
    }.let { PanelSet(it) }

  }
}
