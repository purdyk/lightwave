package lightwave.api.nanoleaf.models


class Power(val on: BoolValue) {
  constructor(on: Boolean) : this(BoolValue(on))
}

class Brightness(val brightness: DurationValue) {
  constructor(newValue: Int) : this(DurationValue(newValue))
}

class Select(val select: String)

data class BoolValue(val value: Boolean)

data class RangeValue(
  val value: Int,
  val max: Int,
  val min: Int
)

data class DurationValue(val value: Int, val duration: Int = 4)

class Effects(val effectsList: List<String>,
              val select: String)

