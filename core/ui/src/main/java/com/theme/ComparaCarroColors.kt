package com.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Stable
class ComparaCarroColors(
    background: Color,
    surface: Color,
    surfaceHeader: Color,
    surfaceLow: Color,
    surfaceRaised: Color,
    surfaceInset: Color,
    surfaceInput: Color,
    surfaceGlass: Color,
    interactivePrimary: Brush,
    interactivePrimarySolid: Color,
    outlineGhost: Color,
    textPrimary: Color,
    textSecondary: Color,
    textInteractive: Color,
    accentPrimary: Color,
    accentTertiary: Color,
    error: Color,
    isDark: Boolean
) {
    var background by mutableStateOf(background, structuralEqualityPolicy())
        private set
    var surface by mutableStateOf(surface, structuralEqualityPolicy())
        private set
    var surfaceHeader by mutableStateOf(surfaceHeader, structuralEqualityPolicy())
        private set
    var surfaceLow by mutableStateOf(surfaceLow, structuralEqualityPolicy())
        private set
    var surfaceRaised by mutableStateOf(surfaceRaised, structuralEqualityPolicy())
        private set
    var surfaceInset by mutableStateOf(surfaceInset, structuralEqualityPolicy())
        private set
    var surfaceInput by mutableStateOf(surfaceInput, structuralEqualityPolicy())
        private set
    var surfaceGlass by mutableStateOf(surfaceGlass, structuralEqualityPolicy())
        private set
    var interactivePrimary by mutableStateOf(interactivePrimary, structuralEqualityPolicy())
        private set
    var interactivePrimarySolid by mutableStateOf(interactivePrimarySolid, structuralEqualityPolicy())
        private set
    var outlineGhost by mutableStateOf(outlineGhost, structuralEqualityPolicy())
        private set
    var textPrimary by mutableStateOf(textPrimary, structuralEqualityPolicy())
        private set
    var textSecondary by mutableStateOf(textSecondary, structuralEqualityPolicy())
        private set
    var textInteractive by mutableStateOf(textInteractive, structuralEqualityPolicy())
        private set
    var accentPrimary by mutableStateOf(accentPrimary, structuralEqualityPolicy())
        private set
    var accentTertiary by mutableStateOf(accentTertiary, structuralEqualityPolicy())
        private set
    var error by mutableStateOf(error, structuralEqualityPolicy())
        private set
    var isDark by mutableStateOf(isDark, structuralEqualityPolicy())
        private set

    fun update(other: ComparaCarroColors) {
        background = other.background
        surface = other.surface
        surfaceHeader = other.surfaceHeader
        surfaceLow = other.surfaceLow
        surfaceRaised = other.surfaceRaised
        surfaceInset = other.surfaceInset
        surfaceInput = other.surfaceInput
        surfaceGlass = other.surfaceGlass
        interactivePrimary = other.interactivePrimary
        interactivePrimarySolid = other.interactivePrimarySolid
        outlineGhost = other.outlineGhost
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        textInteractive = other.textInteractive
        accentPrimary = other.accentPrimary
        accentTertiary = other.accentTertiary
        error = other.error
        isDark = other.isDark
    }
}

@Immutable
private data class GradientPrimary(val start: Color, val end: Color) {
    fun toBrush(): Brush = Brush.linearGradient(
        colors = listOf(start, end),
        start = Offset.Zero,
        end = Offset.Infinite
    )
}

fun darkColors(): ComparaCarroColors {
    val gradient = GradientPrimary(
        start = TokenColors.PrimaryDim,
        end = TokenColors.PrimaryContainer
    ).toBrush()
    return ComparaCarroColors(
        background = TokenColors.Surface,
        surface = TokenColors.SurfaceContainer,
        surfaceHeader = TokenColors.SurfaceHeader,
        surfaceLow = TokenColors.SurfaceContainerLow,
        surfaceRaised = TokenColors.SurfaceContainerHigh,
        surfaceInset = TokenColors.SurfaceContainerLowest,
        surfaceInput = TokenColors.SurfaceContainerHighest,
        surfaceGlass = TokenColors.SurfaceVariant.copy(alpha = 0.6f),
        interactivePrimary = gradient,
        interactivePrimarySolid = TokenColors.PrimaryContainer,
        outlineGhost = TokenColors.Outline.copy(alpha = 0.15f),
        textPrimary = TokenColors.OnSurface,
        textSecondary = TokenColors.OnSurfaceVariant,
        textInteractive = TokenColors.OnPrimaryFixed,
        accentPrimary = TokenColors.PrimaryAccent,
        accentTertiary = TokenColors.Tertiary,
        error = TokenColors.Error,
        isDark = true
    )
}

internal val LocalComparaCarroColors = compositionLocalOf<ComparaCarroColors> {
    error("LocalComparaCarroColors not provided. Wrap content in Theme { ... }.")
}

@Composable
@ReadOnlyComposable
internal fun localColors(): ComparaCarroColors = LocalComparaCarroColors.current
