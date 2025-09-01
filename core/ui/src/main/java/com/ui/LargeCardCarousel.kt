package com.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LargeCardCarousel(
    modifier: Modifier = Modifier,
    items: List<LargeCardType>,
    itemSpacingDp: Int = 7
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(),
        horizontalArrangement = Arrangement.spacedBy(itemSpacingDp.dp)
    ) {
        items(items) { content ->
            content(Modifier)
        }
    }
}


