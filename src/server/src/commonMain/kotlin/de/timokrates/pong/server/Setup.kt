package de.timokrates.pong.server

import de.timokrates.pong.domain.Update
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

fun Game.setupGame(clientChannel: Channel<Client>) = GameService.launch {
    val updateChannel = initializeClients(clientChannel)
    gameLoop(updateChannel)
}

private suspend fun Game.initializeClients(clientChannel: Channel<Client>): Channel<Update> {
    val clients = mutableListOf<Client>()
    repeat(2) {
        clients.add(clientChannel.receive())
    }
    val updateChannel = Channel<Update>()
    GameService.launch {
        for (update in updateChannel) {
            clients.forEach {
                it.output.send(update)
            }
        }
    }
    clients.forEachIndexed { i, it ->
        GameService.launch {
            for (update in it.input) {
                val input = update.input ?: continue
                shared.modify {
                    inputs = inputs.toMutableList().apply {
                        this[i] = input
                    }
                }
            }
        }
    }
    return updateChannel
}
