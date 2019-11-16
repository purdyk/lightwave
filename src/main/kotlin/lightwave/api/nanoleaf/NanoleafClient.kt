package lightwave.api.nanoleaf

import com.google.gson.GsonBuilder
import lightwave.api.nanoleaf.models.Brightness
import lightwave.api.nanoleaf.models.PanelStatus
import lightwave.api.nanoleaf.models.Power
import lightwave.api.nanoleaf.models.Select
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response


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
    put("state", gson.toJson(Power(on))).use {
      if (!it.isSuccessful) { println(it.message) }
    }
  }

  fun brightness(value: Int) {
    put("state", gson.toJson(Brightness(value))).use {
      if (!it.isSuccessful) { println(it.message) }
    }
  }

  fun effect(value: String) {
    put("effects", gson.toJson(Select(value))).use {
      if (!it.isSuccessful) { println(it.message) }
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
        NanoleafStream(gson.fromJson(it, NanoleafStream.Config::class.java))
      }
    }

    return null
  }


}
