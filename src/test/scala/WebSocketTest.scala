import scalaWebSocket.ScalaWebSocket

import java.net.http.WebSocket
import java.net.http.WebSocket.Listener
import java.util.concurrent.CompletionStage

object WebSocketTest extends App {
  val listener: Listener = new Listener {
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
  }

  val ws = new ScalaWebSocket(/*"wss://gateway.discord.gg/?v=9&encoding=json"*/"wss://echo.websocket.org", listener)
  ws.send("Message", true)
  ws.close(1000, "None")
}