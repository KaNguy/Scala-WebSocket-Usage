package scalaWebSocket

/**
 * Created by KaNguy - 06/14/2021
 * File scalaWebSocket.ScalaWebSocket.scala
 */

// Networking & Web
import java.net.http.{HttpClient, HttpHeaders, HttpRequest, HttpResponse, WebSocket}
import java.net.http.WebSocket.{Builder, Listener}
import java.net.{ConnectException, URI}
import java.util.concurrent.CompletableFuture

// New I/O
import java.nio.ByteBuffer

// Utilities
import java.util.concurrent.{CompletionStage, CountDownLatch, TimeUnit}
import java.util.EventListener

// Local
import scalaWebSocket.WebSocketListener
import scalaWebSocket.WebSocketDispatch

class ScalaWebSocket(var url: String = null, var listener: Listener = new WebSocketListener(), connectionTimeout: Int = 1000) {
  private val hasWSProtocol: Boolean = this.hasWebSocketProtocol(url)
  if (!hasWSProtocol) throw new Error("The URL does not have a WebSocket protocol")

  private val latch: CountDownLatch = new CountDownLatch(1)

  private val httpClient: HttpClient = HttpClient.newHttpClient()
  private var webSocket: WebSocket = httpClient.newWebSocketBuilder().buildAsync(URI.create(url), listener).join()

  def send(data: CharSequence, last: Boolean, timeout: Int = 1000): Unit = {
    try {
      this.webSocket.sendText(data, last)
      latch.await(timeout, TimeUnit.MILLISECONDS)
    } catch {
      case error: Throwable => error.printStackTrace()
    }
  }

  def close(statusCode: Int = WebSocket.NORMAL_CLOSURE, reason: String, timeout: Int = 1000): Unit = {
    try {
      this.webSocket.sendClose(statusCode, reason)
      latch.await(timeout, TimeUnit.MILLISECONDS)
    } catch {
      case error: Throwable => error.printStackTrace()
    }
  }

  def ping(message: ByteBuffer): CompletableFuture[WebSocket] = {
    try {
      this.webSocket.sendPing(message)
    } catch {
      case error: Throwable => error.printStackTrace()
      new CompletableFuture[WebSocket]()
    }
  }

  def pong(message: ByteBuffer): CompletableFuture[WebSocket] = {
    try {
      this.webSocket.sendPong(message)
    } catch {
      case error: Throwable => error.printStackTrace()
        new CompletableFuture[WebSocket]()
    }
  }

  def sendBinary(data: ByteBuffer, last: Boolean): CompletableFuture[WebSocket] = {
    try {
      this.webSocket.sendBinary(data, last)
    } catch {
      case error: Throwable => error.printStackTrace()
        new CompletableFuture[WebSocket]()
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
