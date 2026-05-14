package com.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenSpacing

enum class BottomNavTab(val label: String, val icon: ImageVector) {
    Garagem("Garagem", Icons.Filled.DirectionsCar),
    Comparar("Comparar", Icons.AutoMirrored.Filled.CompareArrows),
    Favoritos("Favoritos", Icons.Filled.Favorite),
    Perfil("Perfil", Icons.Filled.Person)
}

@Composable
fun BottomNavBar(
    selected: BottomNavTab,
    onSelect: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Theme.colors.surfaceInset)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .height(72.dp)
                .padding(horizontal = TokenSpacing.Block),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavTab.entries.forEach { tab ->
            BottomNavItem(
                tab = tab,
                isSelected = tab == selected,
                onClick = { onSelect(tab) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    tab: BottomNavTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (isSelected) Theme.colors.accentPrimary else Theme.colors.textSecondary
    Column(
        modifier =
            Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
                .padding(vertical = TokenSpacing.Item, horizontal = TokenSpacing.Inline),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = tab.icon,
            contentDescription = tab.label,
            tint = tint,
            modifier = Modifier.size(TokenIconSize.Medium)
        )
        Box(modifier = Modifier.size(4.dp))
        Text(
            text = tab.label,
            style = Theme.typography.labelMedium,
            color = tint
        )
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    Theme {
        BottomNavBar(
            selected = BottomNavTab.Garagem,
            onSelect = {}
        )
    }
}
