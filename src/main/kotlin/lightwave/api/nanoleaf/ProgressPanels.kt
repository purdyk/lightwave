package lightwave.api.nanoleaf

import kotlin.math.floor


class ProgressPanels(val layout: Panel, val direction: Direction) {

  private val orderIds = when (direction) {
    Direction.Up -> layout.panelIdsByY.reversed()
    Direction.Down -> layout.panelIdsByY
    Direction.Left -> layout.panelIdsByX.reversed()
    Direction.Right -> layout.panelIdsByX
  }

  // Progress is between 1 and 0
  fun generate(progress: Double): PanelSet {
    val perPanelProgress = 1.0 / orderIds.size

    // example 0.5 and 9 panels, we get fract = 4.5, fp = 4, last = 0.5
    val fract = progress / perPanelProgress
    val fp = floor(fract).toInt()
    val last = fract - fp

    return orderIds.mapIndexed { idx, e ->
      when {
        idx < fp -> PanelConfig(e, PanelColor(255, 255, 255))
        idx > fp -> PanelConfig(e, PanelColor(0, 0, 0))
        else -> (255 * last).toInt().let { PanelConfig(e, PanelColor(it, it, it)) }
      }
    }.let { PanelSet(it) }

  }

  enum class Direction {
    Up, Down, Left, Right
  }
}
