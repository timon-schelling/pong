package de.timokrates.pong.domain

import kotlinx.serialization.Serializable

@Serializable
data class Update(val state: State? = null, val input: Input? = null)
