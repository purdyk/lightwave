package lightwave.api.nanoleaf

data class PanelStatus(
  val name: String,
  val serialNo: String,
  val firmwareVersion: String,
  val model: String,
  val effects: Effects,
  val panelLayout: Panel,
  val state: PanelState
)

class Effects(val effectsList: List<String>,
              val select: String)

data class PanelState(
  val brightness: RangeValue,
  val colorMode: String,
  val ct: RangeValue,
  val hue: RangeValue,
  val on: BoolValue,
  val sat: RangeValue
)