package com.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object Theme {
    val colors: ComparaCarroColors
        @Composable
        @ReadOnlyComposable
        get() = LocalComparaCarroColors.current

    val typography: ComparaCarroTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalComparaCarroTypography.current
}

@Composable
fun Theme(
    darkTheme: Boolean = true,
    // To follow the system theme instead: darkTheme: Boolean = isSystemInDarkTheme()
    // (re-add the androidx.compose.foundation.isSystemInDarkTheme import).
    content: @Composable () -> Unit,
) {
    val colors = remember(darkTheme) { if (darkTheme) darkColors() else lightColors() }
    val typography = remember { defaultTypography() }

    val baseColorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            view.context.findActivity()?.window?.let { window ->
                // Dark theme → dark status bar with light (white) icons; light theme → dark icons.
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalComparaCarroColors provides colors,
        LocalComparaCarroTypography provides typography,
    ) {
        MaterialTheme(
            colorScheme =
                baseColorScheme.copy(
                    background = colors.background,
                    surface = colors.surface,
                    surfaceVariant = colors.surfaceRaised,
                    primary = colors.accentPrimary,
                    onPrimary = colors.textInteractive,
                    onBackground = colors.textPrimary,
                    onSurface = colors.textPrimary,
                    error = colors.error,
                ),
            typography = TokenDefaultTypography,
            content = content,
        )
    }
}

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
