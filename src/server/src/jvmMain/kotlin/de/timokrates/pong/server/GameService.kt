package de.timokrates.pong.server

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object GameService {
    private val waitingGames = mutableMapOf<String, Channel<Client>>()
    private val waitingGamesMutex = Mutex()

    fun join(id: String, client: Client) {
        GlobalScope.launch {
            waitingGamesMutex.withLock {
                val clientChannel: Channel<Client>
                if (!waitingGames.contains(id)) {
                    clientChannel = Channel()
                    waitingGames[id] = clientChannel
                    Game().setupGame(clientChannel)
                } else {
                    clientChannel = waitingGames[id] ?: return@launch
                    waitingGames.remove(id)
                }
                clientChannel.send(client)
            }
        }
    }
}