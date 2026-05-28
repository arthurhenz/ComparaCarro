package com.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatReclineNormal
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenSpacing

@Composable
fun CarDetailOptional(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Item)
    ) {
        Icon(
            modifier = Modifier.size(TokenIconSize.Medium),
            imageVector = icon,
            contentDescription = title,
            tint = Theme.colors.textPrimary
        )

        Text(
            text = title,
            color = Theme.colors.textPrimary,
            style = Theme.typography.labelMedium
        )
    }
}

data class OptionalItem(
    val icon: ImageVector,
    val title: String
)

@Composable
fun CarDetailOptionalsList(
    modifier: Modifier = Modifier,
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
        verticalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
        userScrollEnabled = false,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun CarDetailOptionalPreview() {
    Theme {
        CarDetailOptional(
            icon = Icons.Filled.AirlineSeatReclineNormal,
            title = "Banco de Couro"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CarDetailOptionalsListPreview() {
    val optionals =
        listOf(
            OptionalItem(icon = optionalIcon("Banco de Couro"), title = "Banco de Couro"),
            OptionalItem(icon = optionalIcon("Câmera de Ré"), title = "Câmera de Ré"),
            OptionalItem(icon = optionalIcon("GPS"), title = "GPS"),
            OptionalItem(icon = Icons.Filled.WbSunny, title = "Teto Solar")
        )

    Theme {
        CarDetailOptionalsList {
            items(optionals) { optional ->
                CarDetailOptional(
                    icon = optional.icon,
                    title = optional.title
                )
            }
        }
    }
}
