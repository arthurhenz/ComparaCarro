package com.common.utils

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
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

@Composable
fun Modifier.shimmerEffect(isDarkTheme: Boolean = isSystemInDarkTheme()): Modifier =
    composed {
        var size by remember {
            mutableStateOf(IntSize.Zero)
        }
        val transition = rememberInfiniteTransition(label = "shimmerTransition")
        val startOffsetX by transition.animateFloat(
            initialValue = -2 * size.width.toFloat(),
            targetValue = 2 * size.width.toFloat(),
            animationSpec =
                infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "shimmerOffsetX",
        )

        // Choose colors based on theme
        val shimmerColors =
            if (isDarkTheme) {
                listOf(
                    Color(0xFF494956),
                    Color(0xFF5D5D6E),
                    Color(0xFF494956),
                )
            } else {
                listOf(
                    Color(0xFFDADADA),
                    Color(0xFFE8E8E8),
                    Color(0xFFDADADA),
                )
            }

        background(
            brush =
                Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset(startOffsetX, 0f),
                    end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat()),
                ),
        ).onGloballyPositioned {
            size = it.size
        }
    }
