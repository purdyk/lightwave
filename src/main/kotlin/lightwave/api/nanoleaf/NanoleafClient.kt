package lightwave.api.nanoleaf

import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.net.DatagramSocket
import java.net.InetAddress
import java.time.temporal.ValueRange


class NanoleafClient(private val host: String, private val apiKey: String) {
  private val client = OkHttpClient()
  private val json = "application/json; charset=utf-8".toMediaType();
  private val gson = GsonBuilder().create()

  private var currentInfo = info()

  private fun req(path: String) =
    Request.Builder()
      .url("http://$host/api/v1/$apiKey/$path")

  private fun post(path: String, body: String): Response {
    val rBody = body.toRequestBody(json)
    val r = req(path)
      .post(rBody)
      .build()

    return client.newCall(r).execute()
  }

  private fun put(path: String, body: String): Response {
    val rBody = body.toRequestBody(json)
    val r = req(path)
      .put(rBody)
      .build()

    return client.newCall(r).execute()
  }

  private fun get(path: String): Response {
    val r = req(path)
      .build()

    return client.newCall(r).execute()
  }

  fun power(on: Boolean) {
    put("state", gson.toJson(Power(on))).use { r ->
      println(r.message)
    }
  }

  fun brightness(value: Int) {
    put("state", gson.toJson(Brightness(value))).use { r ->
      println(r.message)
    }
  }

  fun info(): PanelStatus? =
    get("").body?.charStream().use {
      gson.fromJson(it, PanelStatus::class.java)
    }


  fun stream(): NanoleafStream? {
    val body = gson.toJson(
      mapOf(
        "write" to
            mapOf(
              "command" to "display",
              "animType" to "extControl"
            )
      )
    )
    val res = put("effects", body)

    if (res.isSuccessful) {
      return res.body?.charStream()?.use {
        NanoleafStream(gson.fromJson(s, NanoleafStreamConfig::class.java))
      }
    }

    return null
  }


}
