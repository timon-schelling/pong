package de.timokrates.pong.server

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    GlobalScope.launch {
        game()
    }
    embeddedServer(Netty) {
        application()
    }.start(wait = true)
}
