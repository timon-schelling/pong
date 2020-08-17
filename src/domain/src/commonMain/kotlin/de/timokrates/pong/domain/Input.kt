package de.timokrates.pong.domain

import kotlinx.serialization.Serializable

@Serializable
enum class Input {
    None,
    Up,
    Down
}
