package de.timokrates.pong.server

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.delay
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    embeddedServer(Netty) {
        application()
    }.start(wait = true)
}
