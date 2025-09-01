package com.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.TokenColors
import com.theme.TokenDefaultTypography

private const val CARD_HEIGHT = 250
private const val CARD_WIDTH = 175

@Composable
public fun CardSmall(
    modifier: Modifier = Modifier,
    background: Painter,
    contentDescription: String = "",
    selected: Boolean,
    onToggleButton: (Boolean) -> Unit,
    title: String,
    price: String,

    ) {
    Column(
        modifier = modifier
            .height(CARD_HEIGHT.dp)
            .width(CARD_WIDTH.dp)
            .clearAndSetSemantics { this.contentDescription = contentDescription },
    ) {
        Box(modifier = Modifier.padding(bottom = 10.dp)) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                painter = background,
                contentDescription = title,
                contentScale = ContentScale.Crop,
            )
            FavoriteButton(
                selected = selected,
                onToggle = onToggleButton,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
            )
        }

        Text(
            text = title,
            color = TokenColors.Title,
            style = TokenDefaultTypography.titleSmall,
            modifier = Modifier
                .fillMaxWidth(),
        )

        Text(
            text = price,
            color = TokenColors.Subtitle,
            style = TokenDefaultTypography.labelMedium,
            modifier = Modifier
                .fillMaxWidth(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SmallCardPreview() {
    CardSmall(
        background = painterResource(id = R.drawable.ic_launcher_background),
        selected = true,
        onToggleButton = { true },
        title = "Saveiro Pega no Breu Audi A4 Sedan 2019",
        price = "R$30.000,00"
    )
}