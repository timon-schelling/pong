package de.timokrates.pong.domain

import kotlinx.serialization.Serializable

@Serializable
data class Ball(val position: Position)
