package scalaWebSocket

/**
 * Created by KaNguy - 06/14/2021
 * File scalaWebSocket.ScalaWebSocket.scala
 */

import java.net.http.{HttpClient, HttpHeaders, HttpRequest, HttpResponse, WebSocket}
import java.net.http.WebSocket.{Builder, Listener}
import java.net.URI
import java.nio.ByteBuffer
import java.util.concurrent.CompletionStage
import java.util.EventListener

class ScalaWebSocket(url: String = null) {
  def connect(url: String = this.url): Unit = {
    val protocol: String = ""
    protocol.toString
  }
}

object ScalaWebSocket extends App {
  println(new ScalaWebSocket().connect("wss://localhost:8080"))
}
