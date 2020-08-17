package de.timokrates.pong.lib.logging

import de.timokrates.pong.lib.event.Event
import de.timokrates.pong.lib.event.Executor
import de.timokrates.pong.lib.logging.receiver.ParentLogReceiver

fun Logger(name: String, parent: Logger? = null, executor: Executor<Log>? = null, reuseTime: Boolean = false) =
        Logger(name, executor).apply {
            if (parent != null) add(ParentLogReceiver(parent, reuseTime))
        }

open class Logger(val name: String, executor: Executor<Log>? = null) {

    private val _onLog = if (executor != null) Event<Log>(executor = executor) else Event<Log>()
    val onLog = _onLog.toSaveEvent()

    fun add(r: LogReceiver) {
        onLog.add(r)
    }

    fun remove(r: LogReceiver) {
        onLog.remove(r)
    }

    fun log(log: Log) {
        if (log.logger === this) _onLog.fire(log)
    }
}
