package scalaWebSocket

/**
 * Created by KaNguy - 06/17/2021
 * File scalaWebSocket.WebSocketEvents.scala
 */

// Networking & web
import java.net.http.WebSocket

// New I/O
import java.nio.ByteBuffer

// Utilities
import java.util.concurrent.CompletionStage

trait WebSocketEvents {
  def onOpen(webSocket: WebSocket): Unit
}
