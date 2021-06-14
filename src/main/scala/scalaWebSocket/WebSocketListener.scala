package scalaWebSocket

/**
 * Created by KaNguy - 06/14/2021
 * File scalaWebSocket.WebSocketListener.scala
 */

import java.net.http.{HttpClient, HttpHeaders, HttpRequest, HttpResponse, WebSocket}
import java.net.http.WebSocket.{Builder, Listener}
import java.net.URI
import java.nio.ByteBuffer
import java.util.concurrent.CompletionStage

case class WebSocketListener() extends Listener {
  override def onOpen(webSocket: WebSocket): Unit = {
    super.onOpen(webSocket)
  }

  override def onText(webSocket: WebSocket, data: CharSequence, last: Boolean): CompletionStage[_] = {
    super.onText(webSocket, data, last)
  }

  override def onPing(webSocket: WebSocket, message: ByteBuffer): CompletionStage[_] = {
    super.onPing(webSocket, message)
  }
}
