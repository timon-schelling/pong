package de.timokrates.pong.lib.event

open class Executor<T> {
    open fun execute(data: T, listeners: List<Listener<T>>) {
        listeners.forEach {
            it(data)
        }
    }
}
