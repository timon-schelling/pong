package de.timokrates.pong.client

import de.timokrates.pong.domain.Update
import kotlinx.coroutines.channels.Channel

class Server(val input: Channel<Update> = Channel(1000), val output: Channel<Update> = Channel(1000))
