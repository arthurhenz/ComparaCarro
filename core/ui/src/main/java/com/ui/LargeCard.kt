/**
 * Created by Arthur Henrique Henz on 08/31/2025
 * Copyright (c) 2025. All rights reserved.
 * Last modified 08/31/2025
 */

package com.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.theme.TokenColors
import com.theme.TokenDefaultTypography

private const val CARD_HEIGHT = 180
private const val CARD_WIDTH = 280
private const val DIAGONAL_ANGLE_DEGREES = 45f

@Composable
fun LargeCard(
    modifier: Modifier = Modifier,
    background: Painter,
    contentDescription: String = "",
    title: String
) {
    Card(
        modifier =
            modifier
                .height(CARD_HEIGHT.dp)
                .width(CARD_WIDTH.dp)
                // drawWithContent
                .clearAndSetSemantics { this.contentDescription = contentDescription },
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = TokenColors.Transparent)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, top = 16.dp)
                        .clip(RoundedCornerShape(10.dp))
            ) {
                Image(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    painter = background,
                    contentDescription = title,
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = title,
                    color = TokenColors.White,
                    style = TokenDefaultTypography.titleSmall,
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp, bottom = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            DiagonalLine()

            RectangleTriangle(
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(top = CARD_HEIGHT.dp * 0.5F + 12.dp)
            )
            RectangleTriangle(
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(start = CARD_HEIGHT.dp * 0.5F + 12.dp)
            )
            Text(
                text = "Recente",
                color = TokenColors.White,
                style = TokenDefaultTypography.titleLarge,
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 34.dp, start = 6.dp)
                        .rotate(-DIAGONAL_ANGLE_DEGREES)
            )
        }
    }
}

@Composable
fun RectangleTriangle(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier.size(16.dp)
    ) {
        val verticalOffset = size.width

        val path =
            Path().apply {
                moveTo(0f, size.height)
                lineTo(size.width, size.height)
                lineTo(size.width, size.height - verticalOffset)
                close()
            }

        drawPath(
            path = path,
            color = TokenColors.TapeBackground
        )
    }
}

@Composable
private fun DiagonalLine(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 40.dp
) {
    Canvas(
        modifier =
            modifier
                .fillMaxSize()
                .clip(ShapeDefaults.Small)
    ) {
        val slope = -1f

        val leftIntersection = size.height * 0.5F
        val rightIntersection = leftIntersection + slope * size.width

        drawLine(
            color = TokenColors.Primary,
            start = Offset(0F, leftIntersection),
            end = Offset(size.width, rightIntersection),
            cap = StrokeCap.Square,
            strokeWidth = strokeWidth.toPx()
        )
    }
}

@Preview()
@Composable
fun LargeCardPreview() {
    LargeCard(
        background = painterResource(id = R.drawable.ic_launcher_background),
        title = "Subaru Impreza Twin Turbo Forged"
    )
}
