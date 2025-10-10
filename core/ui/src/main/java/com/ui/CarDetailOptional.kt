package com.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.TokenColors
import com.theme.TokenDefaultTypography

@Composable
fun CarDetailOptional(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            modifier =
                Modifier
                    .size(20.dp),
            painter = icon,
            contentDescription = title,
            contentScale = ContentScale.Crop
        )

        Text(
            text = title,
            color = TokenColors.Title,
            style = TokenDefaultTypography.titleSmall
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
    optionals: List<OptionalItem>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(optionals) { optional ->
            CarDetailOptional(
                icon = optional.icon,
                title = optional.title
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarDetailOptionalPreview() {
    CarDetailOptional(
        icon = painterResource(id = android.R.drawable.ic_menu_info_details),
        title = "Banco de Couro"
    )
}

@Preview(showBackground = true)
@Composable
fun CarDetailOptionalsListPreview() {
    CarDetailOptionalsList(
        optionals =
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
    )
}
