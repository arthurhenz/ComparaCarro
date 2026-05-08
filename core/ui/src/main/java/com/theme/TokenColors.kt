package com.theme

import androidx.compose.ui.graphics.Color

object TokenColors {
    // --- Stitch dark palette (Brazilian Car Comparator) ---

    // Surface ramp
    val SurfaceContainerLowest = Color(0xFF000000)
    val Surface = Color(0xFF0E0E0E)
    val SurfaceContainerLow = Color(0xFF131313)
    val SurfaceContainer = Color(0xFF1A1A1A)
    val SurfaceContainerHigh = Color(0xFF20201F)
    val SurfaceContainerHighest = Color(0xFF262626)
    val SurfaceBright = Color(0xFF2C2C2C)
    val SurfaceVariant = Color(0xFF262626)

    // Content tones
    val OnSurface = Color(0xFFFFFFFF)
    val OnSurfaceVariant = Color(0xFFADAAAA)
    val Outline = Color(0xFF767575)
    val OutlineVariant = Color(0xFF484847)

    // Accent (primary orange ramp)
    val PrimaryAccent = Color(0xFFFF9064)
    val PrimaryDim = Color(0xFFFF7439)
    val PrimaryContainer = Color(0xFFFF7941)
    val PrimaryFixedDim = Color(0xFFFF5E07)
    val OnPrimaryFixed = Color(0xFF000000)

    // Tertiary (gold)
    val Tertiary = Color(0xFFFFB951)
    val TertiaryContainer = Color(0xFFF8A91F)

    // Error
    val Error = Color(0xFFFF716C)
    val ErrorContainer = Color(0xFF9F0519)

    // --- Legacy tokens (still referenced by com.ui.* composables) ---
    // Removed branch-by-branch as components migrate to Theme.colors.

    val Primary = Color(0xFFFF5C00)
    val White = Color(0xFFFFFFFF)
    val TapeBackground = Color(0xFF7B330A)
    val Title = Color(0xFF000000)
    val OptionalsIcon = Color(0xFF000000)
    val Icon = Color(0xFF000000)
    val Subtitle = Color(0x80000000)
    val Background = Color(0xFFFFFFFF)
    val HeartSelected = Color(0xFFFF5C00)
    val HeartUnselected = Color.Gray.copy(alpha = 0.6f)
    val HeartButtonBorder = Color.Gray.copy(alpha = 0.6f)
    val HeartButtonBackground = Color.White.copy(alpha = 0.7f)
    val Transparent = Color(0x00000000)
}
