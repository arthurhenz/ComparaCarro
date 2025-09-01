package com.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SmallCardList(
    modifier: Modifier = Modifier,
    items: List<CardType>,
    horizontalItemSpacingDp: Int = 16,
    verticalItemSpacingDp: Int = 24
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(horizontalItemSpacingDp.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(verticalItemSpacingDp.dp)
    ) {
        items(items) { content ->
            content(Modifier)
        }
    }
}
