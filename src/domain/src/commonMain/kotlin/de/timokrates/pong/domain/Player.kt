package de.timokrates.pong.domain

import kotlinx.serialization.Serializable

@Serializable
class Player(val position: Position, val size: Double)
