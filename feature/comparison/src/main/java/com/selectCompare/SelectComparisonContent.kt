package com.selectCompare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ui.Header
import com.ui.PrimaryButton
import com.ui.SmallCard

@Composable
fun SelectComparisonContent(
    modifier: Modifier = Modifier,
    state: SelectComparisonScreenState.Success,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    onToggleSelect: (String) -> Unit,
    onCardClick: (String) -> Unit,
    onCompareClick: (Pair<String, String>) -> Unit,
    onLoadMore: (Int) -> Unit
) {
    val selectedIds = state.allSmallCards.filter { it.selected }.map { it.id }
    val isCompareEnabled = selectedIds.size == 2

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp) // Add padding for floating button
        ) {
            Header(
                searchQuery = state.searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onSearchFocusChanged = onSearchFocusChanged,
                isSearchFocused = state.isSearchFocused
            )
            val gridState = rememberLazyGridState()
            LaunchedEffect(gridState.firstVisibleItemIndex, state.smallCards.size) {
                val lastVisibleIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                onLoadMore(lastVisibleIndex)
            }

            LazyVerticalGrid(
                modifier =
                    Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxSize(),
                columns = GridCells.Fixed(2),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = {
                    items(state.smallCards.size) { index ->
                        val cardData = state.smallCards[index]
                        SmallCard(
                            image = painterResource(id = cardData.backgroundRes),
                            selected = cardData.selected,
                            onSelect = { _ -> onToggleSelect(cardData.id) },
                            onClick = { onCardClick(cardData.id) },
                            title = cardData.title,
                            fipe = cardData.fipe
                        )
                    }

                    if (state.isLoadingMore) {
                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.material3.CircularProgressIndicator()
                            }
                        }
                    }
                }
            )
        }

        // Floating button at the bottom
        PrimaryButton(
            text = "Comparar",
            onClick = {
                if (selectedIds.size == 2) {
                    onCompareClick(Pair(selectedIds[0], selectedIds[1]))
                }
            },
            enabled = isCompareEnabled,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
        )
    }
}
