package lightwave.api.nanoleaf.rendering.colors

class Blend(private val a: ColorLAB, private val b: ColorLAB) {

  fun to(pct: Double): ColorLAB {
    return ColorLAB(
      (a.l + ((b.l - a.l) * pct)),
      (a.a + ((b.a - a.a) * pct)),
      (a.b + ((b.b - a.b) * pct)),
      1.0
    )
  }

}