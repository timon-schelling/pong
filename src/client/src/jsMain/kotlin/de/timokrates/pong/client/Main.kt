package de.timokrates.pong.client

import de.timokrates.pong.domain.Update
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement


val canvas = run {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.canvas.width = window.innerWidth
    context.canvas.height = window.innerHeight
    window.addEventListener("resize", {
        context.canvas.width = window.innerWidth
        context.canvas.height = window.innerHeight
    })
    document.body!!.appendChild(canvas)
    canvas
}

fun main() {
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    val server = Server()
    openConnection(server)
    runGame(context, server)
}

private fun openConnection(server: Server) {
    GlobalScope.launch {
        val httpClient = HttpClient(Js) {
            install(WebSockets)
        }
        httpClient.webSocket({ parameter("game", "default") }) {
            try {
                GlobalScope.launch {
                    for (update in server.output) {
                        outgoing.send(Frame.Text(Json.encodeToString(Update.serializer(), update)))
                    }
                }
                for (frame in incoming) {
                    if (frame !is Frame.Text) continue
                    val update = Json.decodeFromString(Update.serializer(), frame.readText())
                    server.input.send(update)
                }
            } catch (e: ClosedReceiveChannelException) {
            } catch (e: Throwable) {
            }
        }
    }
}
