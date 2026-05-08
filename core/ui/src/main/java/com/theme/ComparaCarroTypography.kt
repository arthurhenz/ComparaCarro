package com.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

@Immutable
data class ComparaCarroTypography(
    val headlineLarge: TextStyle,
    val titleLarge: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val priceLarge: TextStyle,
    val priceMedium: TextStyle,
    val labelMedium: TextStyle
)

private val InterFamily = FontFamily.SansSerif
private val SpaceGroteskFamily = FontFamily.Monospace

fun defaultTypography(): ComparaCarroTypography = ComparaCarroTypography(
    headlineLarge = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Bold,
        fontSize = TokenFontSizes.ExtraLarge
    ),
    titleLarge = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Bold,
        fontSize = TokenFontSizes.Large
    ),
    bodyLarge = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Normal,
        fontSize = TokenFontSizes.Medium
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Normal,
        fontSize = TokenFontSizes.Small
    ),
    priceLarge = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Bold,
        fontSize = TokenFontSizes.ExtraLarge
    ),
    priceMedium = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = TokenFontSizes.Medium
    ),
    labelMedium = TextStyle(
        fontFamily = SpaceGroteskFamily,
        fontWeight = FontWeight.Medium,
        fontSize = TokenFontSizes.Small
    )
)

internal val LocalComparaCarroTypography = compositionLocalOf<ComparaCarroTypography> {
    error("LocalComparaCarroTypography not provided. Wrap content in Theme { ... }.")
}

@Composable
@ReadOnlyComposable
internal fun localTypography(): ComparaCarroTypography = LocalComparaCarroTypography.current
