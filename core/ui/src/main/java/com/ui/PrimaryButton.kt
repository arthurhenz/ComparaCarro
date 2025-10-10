/**
 * Created by Arthur Henrique Henz on 08/31/2025
 * Copyright (c) 2025. All rights reserved.
 * Last modified 08/31/2025
 */

package com.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.TokenColors
import com.theme.TokenColors.Primary
import com.theme.TokenDefaultTypography

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary,
            disabledContainerColor = TokenColors.Subtitle.copy(alpha = 0.3f),
            disabledContentColor = TokenColors.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            color = TokenColors.White,
            style = TokenDefaultTypography.bodyLarge
        )
    }
}

@Preview()
@Composable
fun PrimaryButtonPreview() {
    MaterialTheme {
        PrimaryButton(
            text = "Compara",
            onClick = {}
        )
    }
}
