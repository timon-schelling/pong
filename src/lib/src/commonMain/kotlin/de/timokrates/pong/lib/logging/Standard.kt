package de.timokrates.pong.lib.logging

import com.soywiz.klock.DateTime

fun Logger.log(level: LogLevel, message: String) {
    log(level) {
        +message
    }
}

fun Logger.log(level: LogLevel, block: LazyLog.Scope.() -> Unit) {
    log(LazyLog(this, level, DateTime.now(), block))
}

operator fun Logger.invoke(level: LogLevel, block: LazyLog.Scope.() -> Unit) = log(level, block)
fun Logger.trace(block: LazyLog.Scope.() -> Unit) = log(LogLevel.TRACE, block)
fun Logger.debug(block: LazyLog.Scope.() -> Unit) = log(LogLevel.DEBUG, block)
fun Logger.info(block: LazyLog.Scope.() -> Unit) = log(LogLevel.INFO, block)
fun Logger.warn(block: LazyLog.Scope.() -> Unit) = log(LogLevel.WARN, block)
fun Logger.error(block: LazyLog.Scope.() -> Unit) = log(LogLevel.ERROR, block)
fun Logger.exception(e: Throwable) = error { +e.stackTraceToString() }
fun Logger.error(e: Throwable) = exception(e)

operator fun Logger.invoke(level: LogLevel, message: String) = log(level, message)
fun Logger.trace(message: String) = log(LogLevel.TRACE, message)
fun Logger.debug(message: String) = log(LogLevel.DEBUG, message)
fun Logger.info(message: String) = log(LogLevel.INFO, message)
fun Logger.warn(message: String) = log(LogLevel.WARN, message)
fun Logger.error(message: String) = log(LogLevel.ERROR, message)

