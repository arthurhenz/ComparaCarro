/**
 * Created by Arthur Henrique Henz on 08/31/2025
 * Copyright (c) 2025. All rights reserved.
 * Last modified 08/31/2025
 */

package com.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.Theme
import com.theme.TokenIconSize

@Composable
fun FavoriteButton(
    selected: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(40.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onToggle(!selected) },
                )
                .clip(CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .size(28.dp)
                    .background(Theme.colors.surfaceGlass, shape = CircleShape),
        ) {
            Icon(
                imageVector = if (selected) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                contentDescription = null,
                tint = if (selected) Theme.colors.accentPrimary else Theme.colors.textSecondary,
                modifier = Modifier.size(TokenIconSize.Medium),
            )
        }
    }
}

@Preview(name = "Heart Button - Unselected")
@Composable
fun HeartToggleButtonUnselectedPreview() {
    Theme {
        FavoriteButton(
            selected = false,
            onToggle = {},
        )
    }
}

@Preview(name = "Heart Button - Selected")
@Composable
fun HeartToggleButtonSelectedPreview() {
    Theme {
        FavoriteButton(
            selected = true,
            onToggle = {},
        )
    }
}
