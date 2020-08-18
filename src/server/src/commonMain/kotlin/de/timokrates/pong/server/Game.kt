package de.timokrates.pong.server

import de.timokrates.pong.domain.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.milliseconds

class Game : Mutex by Mutex() {

    var playerSpeed = 0.015
    var inputs = listOf(
            Input.None,
            Input.None
    )
    var state = State(
            player = listOf(
                    Player(
                            position = Position(
                                    x = -1.0,
                                    y = 0.0
                            ),
                            size = 0.25
                    ),
                    Player(
                            position = Position(
                                    x = 1.0,
                                    y = 0.0
                            ),
                            size = 0.25
                    )
            ),
            ball = Ball(
                    position = Position(0.0, 0.0)
            )
    )
}

@OptIn(ExperimentalTime::class)
suspend fun Game.gameLoop(update: Channel<Update>) {
    update.send(Update(state = state))
    val run = true
    while (run) {
        val frameTime = measureTime {

            val lastState = state

            val inputs: List<Input>
            this.withLock {
                inputs = this.inputs
            }

            var playerPositionsY = mutableListOf<Double>()
            inputs.forEachIndexed { i, it ->
                var position = when (it) {
                    Input.None -> state.player[i].position.y
                    Input.Up -> state.player[i].position.y + this.playerSpeed
                    Input.Down -> state.player[i].position.y - this.playerSpeed
                }
                if (position < -(1.0 - state.player[i].size)) {
                    position = -(1.0 - state.player[i].size)
                } else if (position > 1.0 - state.player[i].size) {
                    position = 1.0 - state.player[i].size
                }
                playerPositionsY.add(position)
            }

            state = State(
                    player = listOf(
                            Player(
                                    position = Position(
                                            x = state.player[0].position.x,
                                            y = playerPositionsY[0]
                                    ),
                                    size = state.player[0].size
                            ),
                            Player(
                                    position = Position(
                                            x = state.player[1].position.x,
                                            y = playerPositionsY[1]
                                    ),
                                    size = state.player[1].size
                            )
                    ),
                    ball = Ball(
                            position = Position(0.0, 0.0)
                    )
            )

            if (lastState != state) update.send(Update(state = state))
        }
        delay(10.milliseconds - frameTime)
    }
}
