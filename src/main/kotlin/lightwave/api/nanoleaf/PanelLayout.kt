package lightwave.api.nanoleaf

class Panel(
  val globalOrientation: RangeValue,
  private val layout: Layout
) {

  val panelIdsByX: List<Int>
    get() = layout.positionData.sortedBy { it.x }.map { it.panelId }

  val panelIdsByY: List<Int>
    get() = layout.positionData.sortedBy { it.y }.map { it.panelId }

  class Layout(
    val numPanels: Int,
    val sideLength: Int,
    val positionData: List<Position>
  )

  class Position(
    val panelId: Int,
    val x: Int,
    val y: Int,
    val o: Int,
    val shapeType: Int
  )
}


