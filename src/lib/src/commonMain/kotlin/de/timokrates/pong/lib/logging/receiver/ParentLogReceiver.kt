package de.timokrates.pong.lib.logging.receiver

import de.timokrates.pong.lib.logging.LazyLog
import de.timokrates.pong.lib.logging.Log
import de.timokrates.pong.lib.logging.LogReceiver
import de.timokrates.pong.lib.logging.Logger
import com.soywiz.klock.DateTime

class ParentLogReceiver(private val parent: Logger, private val reuseTime: Boolean = false) : LogReceiver() {
    override fun onLog(log: Log) {
        val time = if (reuseTime) log.time else DateTime.now()
        parent.log(LazyLog(parent, log.level, time) {
            +log
        })
    }
}
