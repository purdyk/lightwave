package lightwave.api.nanoleaf


class Power(val on: BoolValue) {
  constructor(on: Boolean): this(BoolValue(on))
}

data class BoolValue(val value: Boolean)

data class RangeValue(
  val value: Int,
  val max: Int,
  val min: Int
)