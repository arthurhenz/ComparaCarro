/**
 * Created by Arthur Henrique Henz on 08/31/2025
 * Copyright (c) 2025. All rights reserved.
 * Last modified 08/31/2025
 */

package com.ui

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theme.TokenColors
import com.theme.TokenIconSize

@Composable
fun FavoriteButton(
    selected: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clickable { onToggle(!selected) },
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(28.dp)
                .background(TokenColors.HeartButtonBackground, shape = CircleShape)
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = TokenColors.HeartSelected,
                    modifier = Modifier.size(TokenIconSize.Medium)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(TokenIconSize.Medium),
                    tint = TokenColors.HeartUnselected
                )
            }
        }
    }
}

@Preview(name = "Heart Button - Unselected")
@Composable
fun HeartToggleButtonUnselectedPreview() {
    FavoriteButton(
        selected = false,
        onToggle = {}
    )
}

@Preview(name = "Heart Button - Selected")
@Composable
fun HeartToggleButtonSelectedPreview() {
    FavoriteButton(
        selected = true,
        onToggle = {}
    )
}
