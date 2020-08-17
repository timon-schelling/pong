package de.timokrates.pong.lib.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun CoroutineContext.scope() = CoroutineScope(this)
inline fun <T> CoroutineContext.scope(block: CoroutineScope.() -> T) = this.scope().block()
suspend operator fun <T> CoroutineContext.invoke(block: suspend CoroutineScope.() -> T) =
        withContext(this, block)