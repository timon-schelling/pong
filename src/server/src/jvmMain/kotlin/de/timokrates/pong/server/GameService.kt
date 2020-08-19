package de.timokrates.pong.server

import de.timokrates.pong.lib.logging.Logger
import de.timokrates.pong.lib.logging.format.Formats
import de.timokrates.pong.lib.logging.receiver.SimpleConsoleLogReceiver
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.reflect.GenericArrayType
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

actual object GameService : CoroutineScope {
    private val waitingGames = mutableMapOf<String, Channel<Client>>()
    private val waitingGamesMutex = Mutex()

    fun join(id: String, client: Client) {
        GlobalScope.launch {
            waitingGamesMutex.withLock {
                val clientChannel: Channel<Client>
                if (!waitingGames.contains(id)) {
                    clientChannel = Channel()
                    waitingGames[id] = clientChannel
                    Game(Logger(id, logger)).setupGame(clientChannel)
                } else {
                    clientChannel = waitingGames[id] ?: return@launch
                    waitingGames.remove(id)
                }
                clientChannel.send(client)
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors(), "GameService")
    actual val logger: Logger = Logger("Game").apply {
        add(SimpleConsoleLogReceiver(formatter = Formats.FlatWithPath))
    }
}
