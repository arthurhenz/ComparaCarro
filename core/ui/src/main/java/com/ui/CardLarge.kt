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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
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
import kotlin.math.atan
import kotlin.math.PI

private const val CARD_HEIGHT = 180
private const val CARD_WIDTH = 308

@Composable
public fun CardLarge(
    modifier: Modifier = Modifier,
    background: Painter,
    contentDescription: String = "",
    title: String
) {
    Box(
        modifier = modifier
            .height(CARD_HEIGHT.dp)
            .width(CARD_WIDTH.dp)
            .clearAndSetSemantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 16.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = background,
                contentDescription = title,
                contentScale = ContentScale.Crop,
            )
            Text(
                text = title,
                color = TokenColors.White,
                style = TokenDefaultTypography.titleSmall,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        val diagonalLineAngle = calculateDiagonalLineAngle()

        DiagonalLine(strokeWidth = 46.dp)

        Text(
            text = "Recent",
            color = TokenColors.White,
            style = TokenDefaultTypography.titleLarge,
            modifier = Modifier
                .rotate(diagonalLineAngle)
                .align(Alignment.TopStart)
                .padding(top = 32.dp, start = 4.dp)

        )
    }
}

/**
 * Helper function to calculate the diagonal line angle
 */
private fun calculateDiagonalLineAngle(): Float {
    val cardWidth = CARD_WIDTH
    val cardHeight = CARD_HEIGHT

    val slope = (0F - cardHeight * 0.4F) / (cardWidth / 3 - 0F)
    val angleRadians = atan(slope)
    return (angleRadians * 180 / PI).toFloat()
}

@Composable
private fun DiagonalLine(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 46.dp
) {
    Canvas(
        modifier = modifier.size(CARD_WIDTH.dp, CARD_HEIGHT.dp)
    ) {
        val slope = (0F - size.height * 0.4F) / (size.width/3 - 0F)

        val leftIntersection = size.height * 0.4F - slope * 0F
        val rightIntersection = size.height * 0.4F + slope * (size.width - 0F)

        drawLine(
            color = TokenColors.Primary,
            start = Offset(0F, leftIntersection),
            end = Offset(size.width, rightIntersection),
            cap = StrokeCap.Square,
            strokeWidth = strokeWidth.toPx()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LargeCardPreview() {
    CardLarge(
        background = painterResource(id = R.drawable.ic_launcher_background),
        title = "Subaru Impreza Twin Turbo Forged",
    )
}