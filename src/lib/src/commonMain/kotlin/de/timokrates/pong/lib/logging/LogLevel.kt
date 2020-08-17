package de.timokrates.pong.lib.logging

enum class LogLevel {
    TRACE {
        override val value = 4
    },
    DEBUG {
        override val value = 3
    },
    INFO {
        override val value = 2
    },
    WARN {
        override val value = 1
    },
    ERROR {
        override val value = 0
    };

    abstract val value: Int
    fun isGreaterOrEqual(level: LogLevel): Boolean {
        if (value <= level.value) return true
        return false
    }
}
