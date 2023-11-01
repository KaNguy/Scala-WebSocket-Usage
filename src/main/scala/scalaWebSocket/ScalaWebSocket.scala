package scalaWebSocket

/**
 * Created by KiyonoKara - 06/14/2021
 * File scalaWebSocket.ScalaWebSocket.scala
 */

// Networking & Web
import java.net.http.{HttpClient, HttpHeaders, HttpRequest, HttpResponse, WebSocket}
import java.net.http.WebSocket.{Builder, Listener}
import java.net.{ConnectException, URI}

// New I/O
import java.nio.{ByteBuffer, CharBuffer}
import java.nio.charset.StandardCharsets

// Utilities
import java.util.concurrent.{CompletableFuture, CompletionStage, CountDownLatch, TimeUnit}

// Local
import scalaWebSocket.WebSocketListener

class ScalaWebSocket(var url: String = null, var listener: Listener = WebSocketListener(), connectionTimeout: Int = 1000) {
  private val hasWSProtocol: Boolean = this.hasWebSocketProtocol(url)
  if (!hasWSProtocol) throw new Error("The URL does not have a WebSocket protocol")

  private val latch: CountDownLatch = new CountDownLatch(1)

  private val httpClient: HttpClient = HttpClient.newHttpClient()
  private var webSocket: WebSocket = httpClient.newWebSocketBuilder().buildAsync(URI.create(url), listener).join()

  val subprotocol: String = webSocket.getSubprotocol

  /**
   * Makes an interaction depending on the action
   * @param action One of the valid WebSocket actions which are SEND, CLOSE, PING, PONG, BINARY
   * @param data Works for the SEND action (the BINARY action has not been tested)
   * @param message Works for the PING and PONG actions
   * @param statusCode Status code for closure
   * @param reason Reason for closure
   * @param last For WebSocket messages
   * @param timeout Timeout for latching
   */
  def interact(action: String = null, data: CharSequence = null, message: ByteBuffer = null, statusCode: Int = WebSocket.NORMAL_CLOSURE, reason: String = "", last: Boolean = false, timeout: Int = 1000): Unit = {
    action.toUpperCase match {
      case "SEND" =>
        this.webSocket.sendText(data, last)
      case "CLOSE" =>
        this.webSocket.sendClose(statusCode, reason)
      case "PING" =>
        this.webSocket.sendPing(message)
      case "PONG" =>
        this.webSocket.sendPong(message)
      case "BINARY" =>
        this.webSocket.sendBinary(ByteBuffer.wrap(data.toString.getBytes(StandardCharsets.UTF_8)), last)
      case _ => ()
    }
    latch.await(timeout, TimeUnit.MILLISECONDS)
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
