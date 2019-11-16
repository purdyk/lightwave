package lightwave.api.nanoleaf.models

data class PanelStatus(
  val name: String,
  val serialNo: String,
  val firmwareVersion: String,
  val model: String,
  val effects: Effects,
  val panelLayout: Panel,
  val state: PanelState
)