package lightwave

import lightwave.api.nanoleaf.BoolValue
import lightwave.api.nanoleaf.NanoleafClient
import lightwave.api.nanoleaf.Power

class Server {

  private val host = System.getenv("LIGHTWAVE_HOST")
  private val auth = System.getenv("LIGHTWAVE_AUTH")

  private val nClient = NanoleafClient(host, auth)

  fun run() {
    println("Server running.")
    nClient.power(false)
    nClient.info()
  }
}