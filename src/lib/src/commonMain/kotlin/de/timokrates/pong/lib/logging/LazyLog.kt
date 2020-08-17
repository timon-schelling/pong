package de.timokrates.pong.lib.logging

import com.soywiz.klock.DateTime

class LazyLog(
        override val logger: Logger,
        override val level: LogLevel,
        override val time: DateTime,
        private val block: Scope.() -> Unit
) : Log {

    private val builder by lazy { Scope(block) }

    override val message: String get() = builder.message
    override val fields: Map<String, String> get() = builder.fields
    override val childes: List<Log> get() = builder.childesList

    class Scope internal constructor(block: Scope.() -> Unit) {

        private val messageBuilder = StringBuilder()

        internal val fields: MutableMap<String, String> = mutableMapOf()

        internal val childesList: MutableList<Log> = mutableListOf()

        init {
            block()
        }

        val message: String = messageBuilder.toString()

        operator fun Log.unaryPlus() = childesList.add(this)

        operator fun String.unaryPlus() {
            messageBuilder.append(this)
        }

        infix fun String.to(value: String) {
            fields[this] = value
        }
    }
}