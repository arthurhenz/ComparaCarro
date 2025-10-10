/**
 * Created by Arthur Henrique Henz on 08/31/2025
 * Copyright (c) 2025. All rights reserved.
 * Last modified 08/31/2025
 */

package com.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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

private const val CARD_HEIGHT = 230
private const val CARD_WIDTH = 175

@Composable
fun SmallCard(
    modifier: Modifier = Modifier,
    image: Painter,
    onClick: () -> Unit,
    contentDescription: String = "",
    title: String,
    fipe: String
) {
    Card(
        modifier =
            modifier
                .height(CARD_HEIGHT.dp)
                .width(CARD_WIDTH.dp)
                .clearAndSetSemantics { this.contentDescription = contentDescription },
        colors =
            CardDefaults.cardColors(
                containerColor = TokenColors.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            ),
        shape = RoundedCornerShape(0.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Box(modifier = Modifier.padding(bottom = 6.dp)) {
                Image(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                    painter = image,
                    contentDescription = title,
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = title,
                color = TokenColors.Title,
                style = TokenDefaultTypography.titleSmall,
                modifier =
                    Modifier
                        .fillMaxWidth()
            )

            Text(
                text = fipe,
                color = TokenColors.Subtitle,
                style = TokenDefaultTypography.labelMedium,
                modifier =
                    Modifier
                        .fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
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
    title: String,
    fipe: String
) {
    Card(
        modifier =
            modifier
                .height(CARD_HEIGHT.dp)
                .width(CARD_WIDTH.dp)
                .clearAndSetSemantics { this.contentDescription = contentDescription },
        colors =
            CardDefaults.cardColors(
                containerColor = TokenColors.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            ),
        shape = RoundedCornerShape(0.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Box(modifier = Modifier.padding(bottom = 6.dp)) {
                Image(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                    painter = image,
                    contentDescription = title,
                    contentScale = ContentScale.Crop
                )
                ToggleSelectButton(
                    selected = selected,
                    onClick = { onSelect(!selected) },
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 4.dp, bottom = 4.dp)
                )
            }

            Text(
                text = title,
                color = TokenColors.Title,
                style = TokenDefaultTypography.titleSmall,
                modifier =
                    Modifier
                        .fillMaxWidth()
            )

            Text(
                text = fipe,
                color = TokenColors.Subtitle,
                style = TokenDefaultTypography.labelMedium,
                modifier =
                    Modifier
                        .fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun SmallCardPreview() {
    Row {
        SmallCard(
            image = painterResource(id = R.drawable.ic_launcher_background),
            selected = true,
            onSelect = { true },
            onClick = {},
            title = "Saveiro Pega no Breu Audi A4 Sedan 2019",
            fipe = "R$30.000,00"
        )

        Spacer(modifier = Modifier.width(8.dp))

        SmallCard(
            image = painterResource(id = R.drawable.ic_launcher_background),
            selected = false,
            onClick = {},
            onSelect = { true },
            title = "Saveiro Pega no Breu Audi A4 Sedan 2019",
            fipe = "R$30.000,00"
        )
    }
}
