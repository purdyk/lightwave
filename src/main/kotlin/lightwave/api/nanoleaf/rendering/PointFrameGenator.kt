package lightwave.api.nanoleaf.rendering

import lightwave.api.nanoleaf.models.Panel
import lightwave.api.nanoleaf.models.PanelSet
import kotlin.math.ceil
import kotlin.math.floor


class PointFrameGenator(
  layout: Panel, direction: Direction,
  private val off: PanelConfig.ColorRGB = PanelConfig.ColorRGB(250, 120, 20),
  private val on: PanelConfig.ColorRGB = PanelConfig.ColorRGB(70, 70, 227)
) : LinearFrameGenerator(layout, direction) {

  private val scale = PanelConfig.Scale(off.lab(), on.lab())

  // Progress is between 1 and 0
  override fun frame(progress: Double): PanelSet {
    val perPanelProgress = 1.0 / (orderIds.size - 1)

    // example 0.5 and 9 panels, we get fract = 4.5, fp = 4, pct = 0.5
    // panel 4 should be half way, and panel 5 should be half way
    val fract = progress / perPanelProgress

    //5.1
    val fp = floor(fract).toInt()
    val sp = ceil(fract).toInt()

    var fPct = sp - fract
    val sPct = fract - fp

    if (fp == sp) {
      fPct = 1.0
    }

    return orderIds.mapIndexed { idx, e ->
      when (idx) {
        fp -> PanelConfig(e, scale.to(fPct).rgb())
        sp -> PanelConfig(e, scale.to(sPct).rgb())
        else -> PanelConfig(e, off)
      }
    }.let { PanelSet(it) }

  }
}
