package de.timokrates.pong.server

import de.timokrates.pong.domain.Update
import kotlinx.coroutines.channels.Channel

data class Client(val input: Channel<Update> = Channel(), val output: Channel<Update> = Channel())
