package de.timokrates.pong.client

import de.timokrates.pong.domain.Input
import de.timokrates.pong.domain.State
import de.timokrates.pong.domain.Update
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.CanvasRenderingContext2D
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

fun runGame(drawContext: CanvasRenderingContext2D, server: Server) {
    handleInput(server)
    val stateChannel = Channel<State?>()
    receiveStateUpdate(server, stateChannel)
    drawContext.drawStateUpdates(stateChannel)
}

private fun handleInput(server: Server) {
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
            server.output.send(Update(input = input))
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
}

@OptIn(ExperimentalTime::class)
private fun receiveStateUpdate(
        server: Server,
        stateChannel: Channel<State?>
) {
    GlobalScope.launch {
        for (update in server.input) {
            update.state?.let { stateChannel.send(it) }
        }
    }
    GlobalScope.launch {
        while (!stateChannel.isClosedForSend) {
            stateChannel.send(null)
            delay(1.seconds)
        }
    }
}

fun CanvasRenderingContext2D.drawStateUpdates(stateChannel: Channel<State?>) {
    fun CanvasRenderingContext2D.drawScaledRect(x: Double, y: Double, w: Double, h: Double) {
        val realX = canvas.width * ((x + 1) / 2)
        val realY = canvas.height - (canvas.height * ((y + 1) / 2))
        val realW = canvas.width * w
        val realH = canvas.height * h
        fillRect(realX - (realW / 2), realY - (realH / 2), realW, realH)
    }

    fun CanvasRenderingContext2D.drawState(state: State) {
        fillStyle = "#000000"
        drawScaledRect(0.0, 0.0, 1.0, 1.0)
        fillStyle = "#FFFFFF"
        state.player.forEach {
            drawScaledRect(it.position.x, it.position.y, 0.025, it.size)
        }
        drawScaledRect(state.ball.position.x, state.ball.position.y, 0.025, 0.025)
    }

    GlobalScope.launch {
        var lastState: State? = null
        for (state in stateChannel) {
            if (state != null) {
                drawState(state)
                lastState = state
            } else if (lastState != null) {
                drawState(lastState)
            }
        }
    }
}
