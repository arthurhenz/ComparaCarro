package com.selectCompare

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ui.Header
import com.ui.PrimaryButton
import com.ui.SmallCard
import com.ui.SmallCardList

@Composable
fun SelectComparisonContent(
    modifier: Modifier = Modifier,
    state: SelectComparisonScreenState.Success,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    onToggleSelect: (String) -> Unit,
    onCardClick: (String) -> Unit,
    onCompareClick: (String) -> Unit
) {
    val selectedIds = state.allSmallCards.filter { it.selected }.map { it.id }
    val isCompareEnabled = selectedIds.size == 2

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
    ) {
        Header(
            onMenuClick = {},
            searchQuery = state.searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearchFocusChanged = onSearchFocusChanged,
            mainHeader = false,
            isSearchFocused = state.isSearchFocused
        )

        val cardHeight = 230.dp
        val verticalSpacing = 24.dp
        val numberOfRows = (state.smallCards.size + 1) / 2
        val totalHeight = (cardHeight * numberOfRows) + (verticalSpacing * (numberOfRows - 1))

        SmallCardList(
            modifier =
                Modifier
                    .padding(horizontal = 24.dp)
                    .height(totalHeight)
        ) {
            items(state.smallCards) { cardData ->
                SmallCard(
                    image = painterResource(id = cardData.backgroundRes),
                    selected = cardData.selected,
                    onSelect = { _ -> onToggleSelect(cardData.id) },
                    onClick = { onCardClick(cardData.id) },
                    title = cardData.title,
                    fipe = cardData.fipe
                )
            }
        }

        PrimaryButton(
            text = "Comparar",
            onClick = {
                if (selectedIds.size == 2) {
                    onCompareClick(selectedIds[1])
                }
            },
            enabled = isCompareEnabled,
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        )
    }
}
