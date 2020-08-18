package de.timokrates.pong.server

import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty) {
        application()
    }.start(wait = true)
}
