package com.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing

private const val CARD_HEIGHT = 260
private const val IMAGE_WIDTH = 140

@Composable
fun LargeCardList(
    image: Painter,
    brand: String,
    model: String,
    fipe: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    powertrain: String = "2.0 Dynamic Force",
    fuel: String = "Híbrido / Flex",
    transmission: String = "CVT 10-Marchas",
    onDetailsClick: (() -> Unit)? = null,
) {
    var favorited by remember { mutableStateOf(false) }

    Row(
        modifier =
            modifier
                .height(CARD_HEIGHT.dp)
                .fillMaxWidth()
                .clip(TokenShapes.StraightEdge)
                .background(Theme.colors.surfaceLow, shape = TokenShapes.StraightEdge)
                .clickable(onClick = onClick)
                .padding(end = TokenSpacing.Inline),
    ) {
        Image(
            painter = image,
            contentDescription = model,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .width(IMAGE_WIDTH.dp)
                    .fillMaxHeight()
                    .clip(TokenShapes.StraightEdge),
        )

        Spacer(modifier = Modifier.width(TokenSpacing.Inline))

        Column(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .padding(vertical = TokenSpacing.Inline),
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = brand.uppercase(),
                        color = Theme.colors.accentPrimary,
                        style = Theme.typography.labelMedium,
                    )
                    Text(
                        text = model.uppercase(),
                        style = Theme.typography.titleLarge,
                        color = Theme.colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                FavoriteButton(
                    selected = favorited,
                    onToggle = { favorited = it },
                )
            }

            Spacer(modifier = Modifier.height(TokenSpacing.Item))

            Text(
                text = "A partir de",
                style = Theme.typography.labelMedium,
                color = Theme.colors.textSecondary,
            )
            Text(
                text = fipe,
                style = Theme.typography.priceMedium,
                color = Theme.colors.accentPrimary,
            )

            Spacer(modifier = Modifier.height(TokenSpacing.Item))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SpecRow(icon = Icons.Filled.Settings, label = powertrain)
                SpecRow(icon = Icons.Filled.LocalGasStation, label = fuel)
                SpecRow(icon = painterResource(R.drawable.manual_transmission), label = transmission)
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Item),
            ) {
                /*ToggleSelectButton(
                    selected = compareSelected,
                    onClick = {
                        compareSelected = !compareSelected
                        onCompareToggle?.invoke(compareSelected)
                    },
                )*/
                PrimaryButton(
                    text = "Ver detalhes",
                    onClick = { onDetailsClick?.invoke() ?: onClick() },
                    modifier = Modifier.height(48.dp),
                )
            }
        }
    }
}

@Composable
private fun SpecRow(icon: ImageVector, label: String) {
    SpecRowContent(label = label) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Theme.colors.textSecondary,
            modifier = Modifier.size(TokenIconSize.Small),
        )
    }
}

@Composable
private fun SpecRow(icon: Painter, label: String) {
    SpecRowContent(label = label) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Theme.colors.textSecondary,
            modifier = Modifier.size(TokenIconSize.Small),
        )
    }
}

@Composable
private fun SpecRowContent(label: String, icon: @Composable () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(modifier = Modifier.width(TokenSpacing.Item))
        Text(
            text = label.uppercase(),
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
