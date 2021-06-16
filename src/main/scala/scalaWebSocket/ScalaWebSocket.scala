package scalaWebSocket

/**
 * Created by KaNguy - 06/14/2021
 * File scalaWebSocket.ScalaWebSocket.scala
 */

// Networking & Web
import java.net.http.{HttpClient, HttpHeaders, HttpRequest, HttpResponse, WebSocket}
import java.net.http.WebSocket.{Builder, Listener}
import java.net.{URI, ConnectException}

// New I/O
import java.nio.ByteBuffer

// Utilities
import java.util.concurrent.CompletionStage
import java.util.EventListener

// Local
import scalaWebSocket.WebSocketListener
import scalaWebSocket.WebSocketDispatch

class ScalaWebSocket(url: String = null, listener: Listener = new WebSocketListener()) {
  private val hasWSProtocol: Boolean = this.hasWebSocketProtocol(url)
  if (!hasWSProtocol) throw new Error("The URL does not have a WebSocket protocol")

  private val httpClient: HttpClient = HttpClient.newHttpClient()
  private var webSocket: WebSocket = httpClient.newWebSocketBuilder().buildAsync(URI.create(url), listener).join()

  def sendText(data: CharSequence, last: Boolean): Unit = {
    try {
      this.webSocket.sendText(data, last)
    } catch {
      case error: Throwable => error.printStackTrace()
    }
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
  val ws = new ScalaWebSocket("wss://echo.websocket.org")
}
