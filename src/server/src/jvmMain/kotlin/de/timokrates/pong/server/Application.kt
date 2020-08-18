package de.timokrates.pong.server

import de.timokrates.pong.domain.Update
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime
import kotlin.time.seconds
import kotlin.time.toJavaDuration

@OptIn(ExperimentalTime::class)
fun Application.application() {
    install(WebSockets) {
        maxFrameSize = Long.MAX_VALUE
        pingPeriod = 60.seconds.toJavaDuration()
        timeout = 60.seconds.toJavaDuration()
    }
    routing {
        webSocket("/") {
            try {
                val gameId = call.parameters["game"] ?: return@webSocket
                val client = Client()
                GameService.join(gameId, client)
                GlobalScope.launch {
                    for (update in client.output) {
                        outgoing.send(Frame.Text(Json.encodeToString(Update.serializer(), update)))
                    }
                }
                for (frame in incoming) {
                    if (frame !is Frame.Text) continue
                    val update = Json.decodeFromString(Update.serializer(), frame.readText())
                    client.input.send(update)
                }
            } catch (e: ClosedReceiveChannelException) {
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        static {
            files(".")
            default("index.html")
        }
    }
}
