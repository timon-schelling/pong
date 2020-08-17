package de.timokrates.pong.lib.logging

import de.timokrates.pong.lib.event.Listener

abstract class LogReceiver : Listener<Log> {
    override fun Log.on() = onLog(this)
    abstract fun onLog(log: Log)
}
