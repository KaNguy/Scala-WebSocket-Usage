package scalaWebSocket

/**
 * Created by KaNguy - 06/14/2021
 * File scalaWebSocket.ScalaWebSocket.scala
 */

import java.net.http.{HttpClient, HttpHeaders, HttpRequest, HttpResponse, WebSocket}
import java.net.http.WebSocket.{Builder, Listener}
import java.net.URI
import java.nio.ByteBuffer
import java.util.concurrent.CompletionStage
import java.util.EventListener

class ScalaWebSocket(url: String = null) {
  def connect(url: String = this.url): Unit = {
    val hasWSProtocol: Boolean = this.hasWebSocketProtocol(url)
    println(hasWSProtocol)
    hasWSProtocol.toString
  }

  private def hasWebSocketProtocol(url: String): Boolean = {
    val webSocketURL: String = url.toLowerCase.trim.replaceAll(" ", "")
    if (webSocketURL.substring(0, 2).equals("ws") || webSocketURL.substring(0, 3).equals("wss")) {
      true
    } else {
      false
    }
  }
}

object ScalaWebSocket extends App {
  new ScalaWebSocket().connect("wss://localhost:8080")
}
