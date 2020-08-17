package de.timokrates.pong.server

import de.timokrates.pong.domain.Input
import de.timokrates.pong.domain.State
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.coroutines.sync.withLock

fun Application.application() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("state") {
            val state: State
            Game.withLock {
                state = Game.state
            }
            call.respond(state)
        }
        route("player") {
            route("{player}") {
                post("input") {
                    val player = call.parameters["player"]?.toInt()
                    if (player != null) {
                        val input = call.receive<Input>()
                        Game.withLock {
                            Game.inputs = Game.inputs.toMutableList().apply {
                                this[player] = input
                            }
                        }
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }
    }
}
