package com.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenSpacing

@Composable
fun CarDetailOptional(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Item)
    ) {
        Image(
            modifier =
                Modifier
                    .size(TokenIconSize.Medium),
            painter = icon,
            contentDescription = title,
            contentScale = ContentScale.Crop
        )

        Text(
            text = title,
            color = Theme.colors.textPrimary,
            style = Theme.typography.labelMedium
        )
    }
}

data class OptionalItem(
    val icon: Painter,
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
            icon = painterResource(id = android.R.drawable.ic_menu_info_details),
            title = "Banco de Couro"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CarDetailOptionalsListPreview() {
    val optionals =
        listOf(
            OptionalItem(
                icon = painterResource(id = android.R.drawable.ic_menu_info_details),
                title = "Banco de Couro"
            ),
            OptionalItem(
                icon = painterResource(id = android.R.drawable.ic_menu_camera),
                title = "Câmera de Ré"
            ),
            OptionalItem(
                icon = painterResource(id = android.R.drawable.ic_menu_compass),
                title = "GPS"
            ),
            OptionalItem(
                icon = painterResource(id = android.R.drawable.ic_menu_gallery),
                title = "Teto Solar"
            )
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
