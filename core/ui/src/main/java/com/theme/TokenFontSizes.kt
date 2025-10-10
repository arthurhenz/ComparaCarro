package com.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object TokenFontSizes {
    val Small = 12.sp
    val Medium = 16.sp
    val Large = 20.sp
    val ExtraLarge = 24.sp
}

val TokenDefaultTypography =
    Typography(
        bodyLarge =
            TextStyle(
                fontSize = TokenFontSizes.Medium,
                fontWeight = FontWeight.Normal
            ),
        titleLarge =
            TextStyle(
                fontSize = TokenFontSizes.Large,
                fontWeight = FontWeight.Bold
            )
    )
