package lightwave.api.nanoleaf.models

data class PanelState(
  val brightness: RangeValue,
  val colorMode: String,
  val ct: RangeValue,
  val hue: RangeValue,
  val on: BoolValue,
  val sat: RangeValue
)