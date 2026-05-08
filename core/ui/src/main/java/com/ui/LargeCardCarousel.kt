package com.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.theme.TokenSpacing

@Composable
fun LargeCardCarousel(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = TokenSpacing.Block),
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Item, Alignment.Start)
    ) {
        content()
    }
}
