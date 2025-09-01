package com.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SmallCardList(
    modifier: Modifier = Modifier,
    horizontalItemSpacingDp: Int = 16,
    verticalItemSpacingDp: Int = 24,
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(),
        horizontalArrangement = Arrangement.spacedBy(horizontalItemSpacingDp.dp),
        verticalArrangement = Arrangement.spacedBy(verticalItemSpacingDp.dp),
        content = content,
        userScrollEnabled = false
    )
}
