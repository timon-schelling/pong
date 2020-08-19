package de.timokrates.pong.server

import de.timokrates.pong.lib.logging.Logger
import kotlinx.coroutines.CoroutineScope

expect object GameService : CoroutineScope {
    val logger: Logger
}
