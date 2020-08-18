package de.timokrates.pong.server

import de.timokrates.pong.domain.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.abs
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.milliseconds

@OptIn(ExperimentalTime::class)
class Game {

    val shared = Shared()

    class Shared : Mutex by Mutex() {

        var inputs = listOf(
                Input.None,
                Input.None
        )

        suspend inline fun modify(block: Shared.() -> Unit) = withLock {
            block()
        }
    }

    private var inputs = listOf(
            Input.None,
            Input.None
    )
    private var state = State(
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
    private var playerSpeed = 0.015
    private var ballInitSpeed = 0.015
    private var ballSpeedX = 0.0
    private var ballSpeedY = 0.0

    private fun frame() {
        val playerPositionsY = mutableListOf<Double>()
        inputs.forEachIndexed { i, it ->
            var position = when (it) {
                Input.None -> state.player[i].position.y
                Input.Up -> state.player[i].position.y + playerSpeed
                Input.Down -> state.player[i].position.y - playerSpeed
            }
            if (position < -(1.0 - state.player[i].size)) {
                position = -(1.0 - state.player[i].size)
            } else if (position > 1.0 - state.player[i].size) {
                position = 1.0 - state.player[i].size
            }
            playerPositionsY.add(position)
        }

        var ballX = state.ball.position.x + ballSpeedX
        var ballY = state.ball.position.y + ballSpeedY

        if (ballSpeedX == 0.0) {
            ballSpeedX = if (Random.nextBoolean()) -ballInitSpeed else ballInitSpeed
        }

        fun collisionBallPlayer(player: Player) {
            if (player != null) {
                if (
                        ballY > player.position.y + player.size ||
                        ballY < player.position.y - player.size
                ) {
                    ballX = 0.0
                    ballY = 0.0
                    ballSpeedX = 0.0
                    ballSpeedY = 0.0
                } else {
                    val ballDistanceToPlayerCenter = abs(ballY - player.position.y)
                    if (ballDistanceToPlayerCenter >= (player.size / 8) * 3) {
                        if (ballY > player.position.y) {
                            ballSpeedY += ballInitSpeed
                        }
                        if (ballY < player.position.y) {
                            ballSpeedY -= ballInitSpeed
                        }
                    } else if (ballDistanceToPlayerCenter >= (player.size / 8) * 2) {
                        if (ballY > player.position.y) {
                            ballSpeedY += ballInitSpeed / 2
                        }
                        if (ballY < player.position.y) {
                            ballSpeedY -= ballInitSpeed / 2
                        }
                    } else if (ballDistanceToPlayerCenter >= (player.size / 8) * 1) {
                        if (ballY > player.position.y) {
                            ballSpeedY += ballInitSpeed / 3
                        }
                        if (ballY < player.position.y) {
                            ballSpeedY -= ballInitSpeed / 3
                        }
                    }
                    if (ballSpeedY > ballInitSpeed) {
                        ballSpeedY = ballInitSpeed
                    }
                    if (ballSpeedY < -ballInitSpeed) {
                        ballSpeedY = -ballInitSpeed
                    }
                }
            }
        }

        if (ballX >= 1) {
            ballSpeedX = -ballSpeedX
            ballX = 1 - (ballX - 1)
            val player = state.player.find { it.position.x == 1.0 }
            if (player != null) {
                collisionBallPlayer(player)
            }
        } else if (ballX <= -1) {
            ballSpeedX = -ballSpeedX
            ballX = -1 + (ballX + 1)
            val player = state.player.find { it.position.x == -1.0 }
            if (player != null) {
                collisionBallPlayer(player)
            }
        }
        if (ballY >= 1) {
            ballSpeedY = -ballSpeedY
            ballY = 1 - (ballY - 1)
        } else if (ballY <= -1) {
            ballSpeedY = -ballSpeedY
            ballY = -1 + (ballY + 1)
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
                        position = Position(ballX, ballY)
                )
        )
    }

    suspend fun gameLoop(update: Channel<Update>) {
        update.send(Update(state = state))
        val run = true
        while (run) {
            val frameTime = measureTime {
                val lastState = state
                copyShared()
                frame()
                if (lastState != state) update.send(Update(state = state))
            }
            delay(10.milliseconds - frameTime)
        }
    }

    private suspend fun copyShared() = shared.modify {
        this@Game.inputs = this.inputs
    }
}
