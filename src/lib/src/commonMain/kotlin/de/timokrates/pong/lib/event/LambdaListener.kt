package de.timokrates.pong.lib.event

class LambdaListener<T>(private val block: T.() -> Unit) : Listener<T> {
    override fun T.on() {
        block()
    }
}
