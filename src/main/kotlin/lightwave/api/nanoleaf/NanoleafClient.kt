package lightwave.api.nanoleaf

import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.net.DatagramSocket
import java.net.InetAddress


class NanoleafClient(private val host: String, private val apiKey: String) {
  private val client = OkHttpClient()
  private val json = "application/json; charset=utf-8".toMediaType();
  private val gson = GsonBuilder().create()

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
      println(r.body?.string())
    }
  }

  fun info() {
    val r = get("")
    println(r.message)
    r.body?.charStream().use {
      val status = gson.fromJson(it, PanelStatus::class.java)
      println(status)
      println(status.panelLayout.panelIdsByY.reversed())
    }
  }

  fun stream() {
//    DatagramSocket(123, InetAddress.getByName())
  }
}
