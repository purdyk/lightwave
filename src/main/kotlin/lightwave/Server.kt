package lightwave

import lightwave.api.nanoleaf.NanoleafClient
import lightwave.api.nanoleaf.rendering.*

class Server {

  private val host = System.getenv("LIGHTWAVE_HOST")
  private val auth = System.getenv("LIGHTWAVE_AUTH")

  private val nClient = NanoleafClient(host, auth)

  fun run() {
    println("Server running.")
    nClient.info()?.let { stat ->
      val bright = stat.state.brightness.value
      val program = stat.effects.select
      val isOn = stat.state.on.value

      nClient.power(true)
      nClient.brightness(stat.state.brightness.max)

      nClient.stream()?.let { stream ->

        val black = PanelConfig.ColorRGB(0, 0, 0)
        val white = PanelConfig.ColorRGB(255, 255, 255)

        PointFrameGenator(
          stat.panelLayout, LinearFrameGenerator.Direction.Down,
          black, white
        ).also { g ->
          FrameLooper(g, 2, 100, 20) {
            stream.push(it)
          }.loop()
        }


        ProgressFrameGenator(
          stat.panelLayout, LinearFrameGenerator.Direction.Up,
          black, white
        ).also { g ->
          FrameLooper(g, 2, 50, 20) {
            stream.push(it)
          }.loop()
        }

        PointFrameGenator(
          stat.panelLayout, LinearFrameGenerator.Direction.Down
        ).also { g ->
          FrameLooper(g, 2, 100, 20) {
            stream.push(it)
          }.loop()
        }

        ProgressFrameGenator(
          stat.panelLayout, LinearFrameGenerator.Direction.Up
        ).also { g ->
          FrameLooper(g, 2, 50, 20) {
            stream.push(it)
          }.loop()
        }

      }

      nClient.brightness(bright)
      nClient.effect(program)

      nClient.power(isOn)
    }
  }
}