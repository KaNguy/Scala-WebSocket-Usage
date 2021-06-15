package scalaWebSocket

/**
 * Created by KaNguy - 06/14/2021
 * File scalaWebSocket.WebSocketDispatch.scala
 */

// Networking & web
import java.net.http.WebSocket

// New I/O
import java.nio.ByteBuffer

// Utilities
import java.util.concurrent.CompletionStage

// Utilities
import java.util.EventListener

trait WebSocketDispatch extends EventListener {
  def onOpen(webSocket: WebSocket): Unit
  def onText(webSocket: WebSocket, data: CharSequence, last: Boolean): CompletionStage[_]
  def onPing(webSocket: WebSocket, message: ByteBuffer): CompletionStage[_]
  def onPong(webSocket: WebSocket, message: ByteBuffer): CompletionStage[_]
}