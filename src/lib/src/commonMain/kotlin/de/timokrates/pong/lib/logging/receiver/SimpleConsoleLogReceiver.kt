package de.timokrates.pong.lib.logging.receiver

import de.timokrates.pong.lib.logging.Log
import de.timokrates.pong.lib.logging.LogLevel
import de.timokrates.pong.lib.logging.LogReceiver
import de.timokrates.pong.lib.logging.format.Formats
import de.timokrates.pong.lib.logging.format.Formatter

class SimpleConsoleLogReceiver(val level: LogLevel = LogLevel.TRACE, val formatter: Formatter = Formats.Default) :
        LogReceiver() {

    override fun onLog(log: Log) {
        if (!log.level.isGreaterOrEqual(level)) return

        println(formatter(log).removeSuffix("\n"))
    }
}
