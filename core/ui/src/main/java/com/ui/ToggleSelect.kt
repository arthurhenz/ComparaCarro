package com.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing

@Composable
fun ToggleSelectButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .clip(TokenShapes.Pill)
                .background(
                    color = if (selected) Theme.colors.interactivePrimarySolid else Theme.colors.surfaceRaised,
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { onClick() }
                .padding(TokenSpacing.Item),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if (selected) Icons.Filled.CheckCircle else Icons.Filled.AddCircle,
            contentDescription = if (selected) "Comparando" else "Comparar",
            tint = if (selected) Theme.colors.textInteractive else Theme.colors.textSecondary,
            modifier = Modifier.size(TokenIconSize.Large),
        )

        AnimatedVisibility(
            visible = selected,
            enter =
                expandHorizontally(
                    animationSpec = tween(durationMillis = 300),
                    expandFrom = Alignment.Start,
                ) + fadeIn(animationSpec = tween(durationMillis = 300)),
            exit =
                shrinkHorizontally(
                    animationSpec = tween(durationMillis = 300),
                    shrinkTowards = Alignment.Start,
                ) + fadeOut(animationSpec = tween(durationMillis = 300)),
        ) {
            Row {
                Spacer(modifier = Modifier.width(TokenSpacing.Item))
                Text(
                    text = "Comparando",
                    style = Theme.typography.labelMedium,
                    color = Theme.colors.textInteractive,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedToggleButtonPreview() {
    Theme {
        Column(
            modifier = Modifier.padding(TokenSpacing.Block),
            verticalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
        ) {
            Text("Unselected state:")
            ToggleSelectButton(
                selected = false,
                onClick = {},
            )

            Text("Selected state:")
            ToggleSelectButton(
                selected = true,
                onClick = {},
            )
        }
    }
}
