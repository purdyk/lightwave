package lightwave.api.nanoleaf.rendering

import lightwave.api.nanoleaf.models.Panel
import lightwave.api.nanoleaf.models.PanelSet

abstract class LinearFrameGenerator(private val layout: Panel, val direction: Direction) {

  protected val orderIds = when (direction) {
    Direction.Up -> layout.panelIdsByY
    Direction.Down -> layout.panelIdsByY.reversed()
    Direction.Left -> layout.panelIdsByX.reversed()
    Direction.Right -> layout.panelIdsByX
  }

  enum class Direction {
    Up, Down, Left, Right
  }

  abstract fun frame(progress: Double): PanelSet
}