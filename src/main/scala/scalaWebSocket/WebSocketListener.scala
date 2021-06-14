package scalaWebSocket

/**
 * Created by KaNguy - 06/14/2021
 * File scalaWebSocket.WebSocketListener.scala
 */

import java.net.http.{HttpClient, HttpHeaders, HttpRequest, HttpResponse, WebSocket}
import java.net.http.WebSocket.{Listener, Builder}
import java.net.URI
import java.util.concurrent.CompletionStage

case class WebSocketListener() extends Listener {
  override def onOpen(webSocket: WebSocket): Unit = {
    super.onOpen(webSocket)
  }

  //override def onText(webSocket: WebSocket, data: CharSequence, last: Boolean):
}
