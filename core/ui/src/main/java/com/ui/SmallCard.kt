/**
 * Created by Arthur Henrique Henz on 08/31/2025
 * Copyright (c) 2025. All rights reserved.
 * Last modified 08/31/2025
 */

package com.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theme.Theme
import com.theme.TokenShapes
import com.theme.TokenSpacing

private const val CARD_HEIGHT = 390
private const val CARD_WIDTH = 175
private const val IMAGE_HEIGHT = 140

@Composable
fun SmallCard(
    modifier: Modifier = Modifier,
    image: Painter,
    onClick: () -> Unit,
    contentDescription: String = "",
    brand: String,
    model: String,
    fipe: String,
) {
    Card(
        modifier =
            modifier
                .height(CARD_HEIGHT.dp)
                .width(CARD_WIDTH.dp)
                .clearAndSetSemantics { this.contentDescription = contentDescription },
        colors =
            CardDefaults.cardColors(
                containerColor = Theme.colors.surface,
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp,
            ),
        shape = TokenShapes.StraightEdge,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxHeight(),
        ) {
            Box(modifier = Modifier.padding(bottom = TokenSpacing.Block)) {
                Image(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(IMAGE_HEIGHT.dp),
                    painter = image,
                    contentDescription = model,
                    contentScale = ContentScale.Crop,
                )
            }

            SmallCardContent(brand = brand, model = model, fipe = fipe)

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                "ver detalhes",
                onClick = onClick,
                modifier =
                    Modifier
                        .padding(bottom = TokenSpacing.Inline)
                        .height(32.dp),
            )
        }
    }
}

@Composable
fun SmallCard(
    modifier: Modifier = Modifier,
    image: Painter,
    contentDescription: String = "",
    selected: Boolean,
    onSelect: (Boolean) -> Unit,
    onClick: () -> Unit,
    brand: String,
    model: String,
    fipe: String,
) {
    Card(
        onClick = onClick,
        modifier =
            modifier
                .height(CARD_HEIGHT.dp)
                .width(CARD_WIDTH.dp)
                .clearAndSetSemantics { this.contentDescription = contentDescription },
        colors =
            CardDefaults.cardColors(
                containerColor = Theme.colors.surface,
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp,
            ),
        shape = TokenShapes.StraightEdge,
    ) {
        Column {
            Box(modifier = Modifier.padding(bottom = TokenSpacing.Item)) {
                Image(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(IMAGE_HEIGHT.dp),
                    painter = image,
                    contentDescription = model,
                    contentScale = ContentScale.Crop,
                )
                ToggleSelectButton(
                    selected = selected,
                    onClick = { onSelect(!selected) },
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 4.dp, bottom = 4.dp),
                )
            }

            SmallCardContent(brand = brand, model = model, fipe = fipe)
        }
    }
}

@Composable
private fun SmallCardContent(
    brand: String,
    model: String,
    fipe: String,
) {
    Column(
        Modifier.padding(horizontal = TokenSpacing.Item),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = brand.uppercase(),
            color = Theme.colors.accentPrimary,
            style = Theme.typography.labelMedium,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = model.uppercase(),
            color = Theme.colors.textPrimary,
            style = Theme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = "Preço sugerido",
            color = Theme.colors.textSecondary,
            style = Theme.typography.labelMedium,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = fipe,
            color = Theme.colors.textPrimary,
            style = Theme.typography.priceMedium,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
fun SmallCardPreview() {
    Theme {
        Row {
            SmallCard(
                image = painterResource(id = R.drawable.ic_launcher_background),
                selected = true,
                onSelect = { true },
                onClick = {},
                brand = "VOLKSWAGEN",
                model = "NIVUS 1.0 200 TSI TOTAL FLEX HIGHLINE AUTOMÁTICO",
                fipe = "R$117.490",
            )

            Spacer(modifier = Modifier.width(TokenSpacing.Item))

            SmallCard(
                image = painterResource(id = R.drawable.ic_launcher_background),
                selected = false,
                onClick = {},
                onSelect = { true },
                brand = "CHEVROLET ",
                model = "CELTA 1.0 MPFI LT 8V FLEX 4P MANUAL\n",
                fipe = "R$35.900",
            )
        }
    }
}
