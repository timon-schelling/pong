package de.timokrates.pong.server

import de.timokrates.pong.domain.Update
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock


fun Game.game(clientChannel: Channel<Client>) = GlobalScope.launch {
    val updateChannel = initializeClients(clientChannel)
    gameLoop(updateChannel)
}

private suspend fun Game.initializeClients(clientChannel: Channel<Client>): Channel<Update> {
    val clients = mutableListOf<Client>()
    repeat(2) {
        clients.add(clientChannel.receive())
    }
    val updateChannel = Channel<Update>()
    GlobalScope.launch {
        for (update in updateChannel) {
            clients.forEach {
                it.output.send(update)
            }
        }
    }
    clients.forEachIndexed { i, it ->
        GlobalScope.launch {
            for (update in it.input) {
                val input = update.input ?: continue
                withLock {
                    inputs = inputs.toMutableList().apply {
                        this[i] = input
                    }
                }
            }
        }
    }
    return updateChannel
}
