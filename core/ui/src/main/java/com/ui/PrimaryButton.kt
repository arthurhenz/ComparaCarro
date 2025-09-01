package com.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theme.TokenColors
import com.theme.TokenColors.Primary
import com.theme.TokenDefaultTypography

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary
        ),
        modifier = modifier
            .width(360.dp)
            .height(64.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            color = TokenColors.White,
            style = TokenDefaultTypography.bodyLarge
        )
    }
}