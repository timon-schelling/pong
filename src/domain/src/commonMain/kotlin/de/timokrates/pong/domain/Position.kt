package de.timokrates.pong.domain

import kotlinx.serialization.Serializable

@Serializable
data class Position(val x: Double, val y: Double)
