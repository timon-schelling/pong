package de.timokrates.pong.client

import de.timokrates.pong.domain.Input
import de.timokrates.pong.domain.State
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.w3c.dom.CanvasRenderingContext2D
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

@OptIn(ExperimentalTime::class)
suspend fun game(drawContext: CanvasRenderingContext2D) {
    val httpClient = HttpClient(Js)
    var up = false
    var down = false
    fun sendInputUpdate() {
        var input = Input.None
        if (up != down) {
            if (up) {
                input = Input.Up
            }
            if (down) {
                input = Input.Down
            }
        }
        GlobalScope.launch {
            httpClient.post<Unit>("http://localhost/player/0/input") {
                contentType(ContentType.Application.Json)
                body = Json.encodeToString(Input.serializer(), input)
            }
        }
    }
    window.onkeydown = {
        if (it.key == "w") {
            up = true
        }
        if (it.key == "s") {
            down = true
        }
        sendInputUpdate()
    }
    window.onkeyup = {
        if (it.key == "w") {
            up = false
        }
        if (it.key == "s") {
            down = false
        }
        sendInputUpdate()
    }
    var lastStateString = ""
    var run = true
    var c = 0
    while (run) {
        val stateString = httpClient.get<String>("http://localhost/state")
        val state = Json.decodeFromString(State.serializer(), stateString)
        if (c >= 10 || lastStateString != stateString) {
            drawContext.fillStyle = "#000000"
            drawContext.drawScaledRect(0.0, 0.0, 1.0, 1.0)
            drawContext.fillStyle = "#FFFFFF"
            state.player.forEach {
                drawContext.drawScaledRect(it.position.x, it.position.y, 0.025, it.size)
            }
            drawContext.drawScaledRect(state.ball.position.x, state.ball.position.y, 0.025, 0.025)
            c = 0
        } else {
            delay(10.milliseconds)
            c++
        }
        lastStateString = stateString
    }
}

fun CanvasRenderingContext2D.drawScaledRect(x: Double, y: Double, w: Double, h: Double) {
    val realX = canvas.width * ((x + 1) / 2)
    val realY = canvas.height - (canvas.height * ((y + 1) / 2))
    val realW = canvas.width * w
    val realH = canvas.height * h
    fillRect(realX - (realW / 2), realY - (realH / 2), realW, realH)
}
