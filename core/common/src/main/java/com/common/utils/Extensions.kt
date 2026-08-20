package com.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * Converts a [Flow] into a [StateFlow] with a specified initial value and a sharing strategy
 * that keeps the flow active while it is subscribed to. The flow will remain active for a
 * specified timeout after the last subscriber unsubscribes, to avoid frequent restarts.
 *
 * @param scope The [CoroutineScope] in which the [StateFlow] will be active.
 * @param initialValue The initial value of the [StateFlow].
 * @param stopTimeoutMillis The timeout in milliseconds to keep the flow active after the last
 * subscriber unsubscribes. Defaults to 5 seconds.
 * @return A [StateFlow] that shares the upstream flow while subscribed.
 */
fun <T> Flow<T>.stateInWhileSubscribed(
    scope: CoroutineScope,
    initialValue: T,
    stopTimeoutMillis: Long = 5_000L
): StateFlow<T> {
    return stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis),
        initialValue = initialValue,
    )
}
