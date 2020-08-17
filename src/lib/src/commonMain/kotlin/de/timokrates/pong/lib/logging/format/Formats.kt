package de.timokrates.pong.lib.logging.format

import de.timokrates.pong.lib.logging.Log
import de.timokrates.pong.lib.time.toTimeStamp
import kotlinx.serialization.json.*

object Formats {

    object Default : Formatter {

        override fun Log.format(): String = buildString {
            append("${time.toTimeStamp()} ${level.name} [${logger.name}]")
            if (fields.isNotEmpty()) {
                append(" { ")
                fields.entries.forEachIndexed { i, e ->
                    if (i > 0) append(", ")
                    append("\"${e.key}\": \"${e.value}\"")
                }
                append(" } ")
            }
            append(if (message.isBlank()) ":" else ": $message")
            if (childes.isEmpty()) return@buildString
            append("\n")
            append(
                    buildString {
                        childes.forEachIndexed { i, it ->
                            if (i > 0) append("\n")
                            append(it.format())
                        }
                    }.prependIndent()
            )
        }
    }

    object Flat : Formatter {

        override fun Log.format(): String = buildString {
            append("${time.toTimeStamp()} ${level.name} [${logger.name}]")
            if (fields.isNotEmpty()) {
                append(" { ")
                fields.entries.forEachIndexed { i, e ->
                    if (i > 0) append(", ")
                    append("\"${e.key}\": \"${e.value}\"")
                }
                append(" } ")
            }
            append(if (message.isBlank()) ":" else ": $message")
            if (childes.isEmpty()) return@buildString
            append("\n")
            childes.forEachIndexed { i, it ->
                if (i > 0) append("\n")
                append(it.format())
            }
        }
    }

    object FlatWithPath : Formatter {

        override fun Log.format(): String = formatWithLoggerPrefix()
        private fun Log.formatWithLoggerPrefix(prefix: String = ""): String = buildString {
            append("${time.toTimeStamp()} ${level.name} [$prefix${logger.name}]")
            if (fields.isNotEmpty()) {
                append(" { ")
                fields.entries.forEachIndexed { i, e ->
                    if (i > 0) append(", ")
                    append("\"${e.key}\": \"${e.value}\"")
                }
                append(" } ")
            }
            append(if (message.isBlank()) ":" else ": $message")
            if (childes.isEmpty()) return@buildString
            append("\n")
            childes.forEachIndexed { i, it ->
                if (i > 0) append("\n")
                append(it.formatWithLoggerPrefix("$prefix${logger.name}|"))
            }
        }
    }

    object Minimal : Formatter {

        override fun Log.format(): String = buildString {
            if (message.isBlank() && fields.isEmpty()) {
                if (childes.isEmpty()) return@buildString
                childes.forEachIndexed { i, it ->
                    if (i > 0) append("\n")
                    append(it.format())
                }
                return@buildString
            }
            append("${time.toTimeStamp()} ${level.name} [${logger.name}]")
            if (fields.isNotEmpty()) {
                append(" { ")
                fields.entries.forEachIndexed { i, e ->
                    if (i > 0) append(", ")
                    append("\"${e.key}\": \"${e.value}\"")
                }
                append(" } ")
            }
            append(": $message")
            if (childes.isEmpty()) return@buildString
            childes.forEach {
                append("\n")
                append(it.format())
            }
        }
    }

    object MinimalWithPath : Formatter {

        override fun Log.format(): String = formatWithLoggerPrefix()
        private fun Log.formatWithLoggerPrefix(prefix: String = ""): String = buildString {
            if (message.isBlank() && fields.isEmpty()) {
                if (childes.isEmpty()) return@buildString
                childes.forEachIndexed { i, it ->
                    if (i > 0) append("\n")
                    append(it.formatWithLoggerPrefix("$prefix${logger.name}|"))
                }
                return@buildString
            }
            append("${time.toTimeStamp()} ${level.name} [$prefix${logger.name}]")
            if (fields.isNotEmpty()) {
                append(" { ")
                fields.entries.forEachIndexed { i, e ->
                    if (i > 0) append(", ")
                    append("\"${e.key}\": \"${e.value}\"")
                }
                append(" } ")
            }
            append(": $message")
            if (childes.isEmpty()) return@buildString
            childes.forEach {
                append("\n")
                append(it.formatWithLoggerPrefix("$prefix${logger.name}|"))
            }
        }
    }

    object Json : Formatter {

        override fun Log.format() = formatJson().toString()

        private fun Log.formatJson(): JsonObject = buildJsonObject {
            put("level", level.name)
            put("logger", logger.name)
            put("time", time.toTimeStamp())
            put("message", message)
            if (childes.isNotEmpty()) {
                put("childes", buildJsonArray {
                    childes.forEach {
                        add(it.formatJson())
                    }
                })
            }
            if (fields.isNotEmpty()) {
                put("fields", buildJsonObject {
                    fields.forEach {
                        put(it.key, it.value)
                    }
                })
            }
        }
    }

    object PrettyJson : Formatter {

        override fun Log.format(): String {
            return Json { prettyPrint = true }.encodeToString(JsonObjectSerializer, formatJson())
        }

        private fun Log.formatJson(): JsonObject = buildJsonObject {
            put("level", level.name)
            put("logger", logger.name)
            put("time", time.toTimeStamp())
            put("message", message)
            if (childes.isNotEmpty()) {
                put("childes", buildJsonArray {
                    childes.forEach {
                        add(it.formatJson())
                    }
                })
            }
            if (fields.isNotEmpty()) {
                put("fields", buildJsonObject {
                    fields.forEach {
                        put(it.key, it.value)
                    }
                })
            }
        }
    }
}
