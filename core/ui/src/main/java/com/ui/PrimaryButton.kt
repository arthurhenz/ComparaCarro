/**
 * Created by Arthur Henrique Henz on 08/31/2025
 * Copyright (c) 2025. All rights reserved.
 * Last modified 08/31/2025
 */

package com.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.Theme
import com.theme.TokenShapes
import com.theme.TokenSpacing

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val backgroundModifier =
        if (enabled) {
            Modifier.background(brush = Theme.colors.interactivePrimary, shape = TokenShapes.Button)
        } else {
            Modifier.background(color = Theme.colors.surfaceRaised, shape = TokenShapes.Button)
        }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = TokenSpacing.Block)
                .height(54.dp)
                .clip(TokenShapes.Button)
                .then(backgroundModifier)
                .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (enabled) Theme.colors.textInteractive else Theme.colors.textSecondary,
            style = Theme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    Theme {
        PrimaryButton(
            text = "Compara",
            onClick = {}
        )
    }
}
