package de.timokrates.pong.lib.logging.format

import de.timokrates.pong.lib.logging.Log

interface Formatter {
    operator fun invoke(log: Log): String = log.format()
    fun Log.format(): String
}
