package de.timokrates.pong.lib.event

interface Listener<T> {
    operator fun invoke(data: T) = data.on()
    fun T.on()
}
