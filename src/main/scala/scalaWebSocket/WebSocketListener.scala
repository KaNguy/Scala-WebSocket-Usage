package scalaWebSocket

/**
 * Created by KaNguy - 06/14/2021
 * File scalaWebSocket.WebSocketListener.scala
 */

// Networking & web
import java.net.http.WebSocket
import java.net.http.WebSocket.Listener

// New I/O
import java.nio.ByteBuffer

// Utilities
import java.util.concurrent.CompletionStage

case class WebSocketListener() extends Listener {
  override def onOpen(webSocket: WebSocket): Unit = {
    webSocket.request(1)
    super.onOpen(webSocket)
  }

  override def onText(webSocket: WebSocket, data: CharSequence, last: Boolean): CompletionStage[_] = {
    webSocket.request(1)
    super.onText(webSocket, data, last)
  }

  override def onPing(webSocket: WebSocket, message: ByteBuffer): CompletionStage[_] = {
    super.onPing(webSocket, message)
  }

  override def onPong(webSocket: WebSocket, message: ByteBuffer): CompletionStage[_] = {
    super.onPong(webSocket, message)
  }

  override def onClose(webSocket: WebSocket, statusCode: Int, reason: String): CompletionStage[_] = {
    super.onClose(webSocket, statusCode, reason)
  }

  override def onError(webSocket: WebSocket, error: Throwable): Unit = {
    super.onError(webSocket, error)
  }

  override def onBinary(webSocket: WebSocket, data: ByteBuffer, last: Boolean): CompletionStage[_] = {
    webSocket.request(1)
    super.onBinary(webSocket, data, last)
  }
}
