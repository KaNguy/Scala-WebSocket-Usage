import scalaWebSocket.ScalaWebSocket
import scalaWebSocket.WebSocketListener

import java.net.http.WebSocket
import java.net.http.WebSocket.Listener
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
      println("Connection closed: " + statusCode + ", " + reason)
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

  val ws = new ScalaWebSocket("wss://echo.websocket.org", wsListener)
  ws.send("Message", true)
  ws.send("Another message", true)
  ws.send("Final message", true)
  ws.interact("SEND", "Interaction", last = true)
  ws.interact("CLOSE", statusCode = 1000, reason = "Normal closure")
  ws.close(1000, "None")
}