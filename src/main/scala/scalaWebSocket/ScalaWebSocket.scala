package scalaWebSocket

/**
 * Created by KiyonoKara - 06/14/2021
 * File scalaWebSocket.ScalaWebSocket.scala
 */

// Networking & Web
import java.net.URI
import java.net.http.WebSocket.Listener
import java.net.http.{HttpClient, WebSocket}

// New I/O
import java.nio.ByteBuffer

// Utilities
import java.util.concurrent.{CompletableFuture, CountDownLatch, TimeUnit}

// Local

class ScalaWebSocket(var url: String = "", var listener: Listener = WebSocketListener()) {
  private val hasWSProtocol: Boolean = this.hasWebSocketProtocol(url)
  if (!hasWSProtocol) throw new Error("The URL does not have a WebSocket protocol")

  private val latch: CountDownLatch = new CountDownLatch(1)

  private val httpClient: HttpClient = HttpClient.newHttpClient()
  private val webSocketBuild: CompletableFuture[WebSocket] = httpClient.newWebSocketBuilder().buildAsync(URI.create(url), listener)
  private var webSocket: WebSocket = webSocketBuild.join()

  def join(): Unit = {
    val newWSBuild = this.httpClient.newWebSocketBuilder().buildAsync(URI.create(url), listener)
    this.webSocket = newWSBuild.join()
  }

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

  def ping(message: ByteBuffer, timeout: Int = 1000): CompletableFuture[WebSocket] = {
    try {
      this.webSocket.sendPing(message)
      latch.await(timeout, TimeUnit.MILLISECONDS)
      new CompletableFuture[WebSocket]()
    } catch {
      case error: Throwable => error.printStackTrace()
      new CompletableFuture[WebSocket]()
    }
  }

  def pong(message: ByteBuffer, timeout: Int = 1000): CompletableFuture[WebSocket] = {
    try {
      this.webSocket.sendPong(message)
      latch.await(timeout, TimeUnit.MILLISECONDS)
      new CompletableFuture[WebSocket]()
    } catch {
      case error: Throwable => error.printStackTrace()
        new CompletableFuture[WebSocket]()
    }
  }

  def sendBinary(data: ByteBuffer, last: Boolean, timeout: Int = 1000): CompletableFuture[WebSocket] = {
    try {
      this.webSocket.sendBinary(data, last)
      latch.await(timeout, TimeUnit.MILLISECONDS)
      new CompletableFuture[WebSocket]()
    } catch {
      case error: Throwable => error.printStackTrace()
        new CompletableFuture[WebSocket]()
    }
  }

  /**
   * Checks if the URL has ws:// or wss://
   * @param url URL
   * @return Boolean whether the URL has the protocol or not
   */
  private def hasWebSocketProtocol(url: String): Boolean = {
    val webSocketURL: String = url.toLowerCase.trim.replaceAll(" ", "")
    webSocketURL.startsWith("ws") || webSocketURL.startsWith("wss")
  }
}
