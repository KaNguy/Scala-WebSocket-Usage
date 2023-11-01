import scalaWebSocket.{ScalaWebSocket, WebSocketListener}

import java.net.http.WebSocket
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletionStage

object WebSocketTest extends App {
  val wsListener = new WebSocketListener {
    override def onOpen(webSocket: WebSocket): Unit = {
      println("WebSocket connection opened")
      super.onOpen(webSocket)
    }

    override def onText(webSocket: WebSocket, data: CharSequence, last: Boolean): CompletionStage[_] = {
      println("Data: " + data)
      super.onText(webSocket, data, last)
    }

    override def onClose(webSocket: WebSocket, statusCode: Int, reason: String): CompletionStage[_] = {
      var formattedReason: String = reason
      if (reason.isEmpty) formattedReason = "No reason"
      println("Connection closed: " + statusCode + ", " + formattedReason)
      super.onClose(webSocket, statusCode, reason)
    }

    override def onError(webSocket: WebSocket, error: Throwable): Unit = {
      println("Error: " + error)
      super.onError(webSocket, error)
    }

    override def onBinary(webSocket: WebSocket, data: ByteBuffer, last: Boolean): CompletionStage[_] = {
      println("Decoded data: " + StandardCharsets.UTF_8.decode(data).toString)
      super.onBinary(webSocket, data, last)
    }
  }

  // Test WebSocket connection and interaction with Postman Websockets
  // https://www.postman.com/postman/workspace/websockets/overview
  val ws = new ScalaWebSocket("wss://ws.postman-echo.com/raw", wsListener)

  // Messages in one part
  ws.send("Message", last = true)
  ws.send("Another message", last = true)
  ws.send("Final message", last = true)

  // Multi-part message
  ws.send("Part 1", last = false)
  ws.send(" & ", last = false)
  ws.send("Part 2", last = true)

  // Ping & Pong
  ws.ping(ByteBuffer.wrap("Ping".getBytes(StandardCharsets.UTF_8)))
  ws.pong(ByteBuffer.wrap("Pong".getBytes(StandardCharsets.UTF_8)))

  // Close connection
  ws.close(WebSocket.NORMAL_CLOSURE, "None")

  // Rejoin
  ws.join()
  // Sending binaries
  ws.sendBinary(ByteBuffer.wrap("[Data]".getBytes(StandardCharsets.UTF_8)), last = true)
  ws.close(WebSocket.NORMAL_CLOSURE, "None")
}