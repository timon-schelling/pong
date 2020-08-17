package de.timokrates.pong.lib.logging

import com.soywiz.klock.DateTime

interface Log {
    val logger: Logger
    val level: LogLevel
    val time: DateTime
    val message: String
    val fields: Map<String, String>
    val childes: List<Log>
}
