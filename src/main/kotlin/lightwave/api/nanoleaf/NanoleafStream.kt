package lightwave.api.nanoleaf

import lightwave.api.nanoleaf.models.PanelSet
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress

class NanoleafStream(config: Config) {
  private val socket = DatagramSocket()
  private var lastPanelSet: PanelSet? = null
  private val remote = InetSocketAddress(InetAddress.getByName(config.streamControlIpAddr), config.streamControlPort)


  fun push(set: PanelSet) {
    val toPush = set.diff(lastPanelSet)
    lastPanelSet = set

//    println("Sending ${toPush.count()} panel commands")

    val bytes = toPush.toByteArray()
    socket.send(DatagramPacket(bytes, bytes.size, remote))
  }

  class Config(
    val streamControlIpAddr: String,
    val streamControlPort: Int,
    val streamControlProtocol: String
  )
}