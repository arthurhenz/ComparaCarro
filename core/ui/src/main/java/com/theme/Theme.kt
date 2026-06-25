package com.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

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
    content: @Composable () -> Unit,
) {
    val colors = remember(darkTheme) { if (darkTheme) darkColors() else lightColors() }
    val typography = remember { defaultTypography() }

    val baseColorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()

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
