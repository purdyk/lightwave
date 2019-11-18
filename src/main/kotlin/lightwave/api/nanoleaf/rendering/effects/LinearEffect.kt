package lightwave.api.nanoleaf.rendering.effects

import lightwave.api.nanoleaf.models.Panel

abstract class LinearEffect(layout: Panel, private val direction: Direction): Effect(layout) {

  protected val orderIds = when (direction) {
    Direction.Up -> layout.panelIdsByY
    Direction.Down -> layout.panelIdsByY.reversed()
    Direction.Left -> layout.panelIdsByX.reversed()
    Direction.Right -> layout.panelIdsByX
  }

  enum class Direction {
    Up, Down, Left, Right
  }
}