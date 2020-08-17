package de.timokrates.pong.domain

import kotlinx.serialization.Serializable

@Serializable
data class State(val player: List<Player>, val ball: Ball)
