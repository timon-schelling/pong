package de.timokrates.pong.domain

import kotlinx.serialization.Serializable

@Serializable
data class Player(val position: Position, val size: Double)
