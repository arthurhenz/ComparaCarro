package com.ui
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
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
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(28.dp)
            .background(TokenColors.HeartButtonBackground, shape = CircleShape)
            .clickable { onToggle(!selected) }
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = TokenColors.HeartSelected,
                modifier = Modifier.size(TokenIconSize.Small)
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = TokenColors.HeartUnselected
            )
            }
    }
}

@Preview(showBackground = false, name = "Heart Button - Unselected")
@Composable
fun HeartToggleButtonUnselectedPreview() {
    FavoriteButton(
        selected = false,
        onToggle = {}
    )
}

@Preview(showBackground = false, name = "Heart Button - Selected")
@Composable
fun HeartToggleButtonSelectedPreview() {
    FavoriteButton(
        selected = true,
        onToggle = {}
    )
}