package lightwave

import lightwave.api.nanoleaf.BoolValue
import lightwave.api.nanoleaf.NanoleafClient
import lightwave.api.nanoleaf.Power
import lightwave.api.nanoleaf.ProgressPanels

class Server {

  private val host = System.getenv("LIGHTWAVE_HOST")
  private val auth = System.getenv("LIGHTWAVE_AUTH")

  private val nClient = NanoleafClient(host, auth)

  fun run() {
    println("Server running.")
    nClient.info()?.let { stat ->
      nClient.power(true)
      nClient.brightness(stat.state.brightness.max)

      nClient.stream()?.let { stream ->
        val prog = ProgressPanels(stat.panelLayout, ProgressPanels.Direction.Up)
        for (i in 0..100) {
          stream.push(prog.generate(i / 100.0))
          Thread.sleep(200)
        }
      }

      nClient.brightness(20)
      nClient.power(false)
    }
  }
}