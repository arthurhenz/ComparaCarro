package com.theme

import androidx.compose.ui.graphics.Color

object TokenColors {
    // --- Stitch dark palette (Brazilian Car Comparator) ---

    // Surface ramp
    val SurfaceContainerLowest = Color(0xFF000000)
    val SurfaceHeader = Color(0xFF0D0D0D)
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
    val PrimaryAccent = Color(0xFFFF5C00)
    val PrimaryDim = Color(0xFFFF7439)
    val PrimaryContainer = Color(0xFFFF5C00)
    val PrimaryFixedDim = Color(0xFFFF5E07)
    val OnPrimaryFixed = Color(0xFF000000)

    // Tertiary (gold)
    val Tertiary = Color(0xFFFFB951)
    val TertiaryContainer = Color(0xFFF8A91F)

    // Error
    val Error = Color(0xFFFF716C)
    val ErrorContainer = Color(0xFF9F0519)

    // --- Legacy tokens still referenced by com.ui.* composables ---
    val TapeBackground = Color(0xFF7B330A)
}
