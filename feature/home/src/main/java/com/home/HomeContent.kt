package com.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.data.model.LargeCardData
import com.data.model.SmallCardData
import com.theme.TokenColors
import com.ui.Header
import com.ui.LargeCard
import com.ui.LargeCardCarousel
import com.ui.PrimaryButton
import com.ui.SmallCard
import com.ui.SmallCardList
import kotlin.collections.forEach

@Composable
fun HomeContent(
    smallCards: List<SmallCardData>,
    recentlyViewedCards: List<LargeCardData> = emptyList(),
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSearchFocusChanged: (Boolean) -> Unit = {},
    isSearchFocused: Boolean = false,
    sortType: SortType = SortType.MOST_POPULAR,
    onSortTypeChange: (SortType) -> Unit = {},
    onCardClick: (String) -> Unit,
    onCompareClick: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Header(
            onMenuClick = {},
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearchFocusChanged = onSearchFocusChanged,
            isSearchFocused = isSearchFocused
        )

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource
                        ) {
                            if (isSearchFocused) {
                                focusManager.clearFocus()
                            }
                        },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (searchQuery.isEmpty()) {
                    if (recentlyViewedCards.isNotEmpty()) {
                        LargeCardCarousel(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                        ) {
                            recentlyViewedCards.forEach { cardData ->
                                item {
                                    LargeCard(
                                        modifier = Modifier.clickable { onCardClick(cardData.id) },
                                        background = painterResource(id = cardData.backgroundRes),
                                        title = cardData.title
                                    )
                                }
                            }
                        }

                        PrimaryButton(
                            text = "Comparar",
                            onClick = onCompareClick,
                            modifier =
                                Modifier
                                    .padding(bottom = 8.dp)
                        )
                    }

                    var dropdownExpanded by remember { mutableStateOf(false) }

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp, horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text =
                                when (sortType) {
                                    SortType.MOST_POPULAR -> "Mais populares"
                                    SortType.ALPHABETIC -> "Alfabética"
                                },
                            style = MaterialTheme.typography.titleLarge
                        )
                        Box {
                            Text(
                                text = "Ordenar",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TokenColors.Subtitle,
                                modifier = Modifier.clickable { dropdownExpanded = true }
                            )

                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Mais populares") },
                                    onClick = {
                                        onSortTypeChange(SortType.MOST_POPULAR)
                                        dropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Alfabética") },
                                    onClick = {
                                        onSortTypeChange(SortType.ALPHABETIC)
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                val cardHeight = 230.dp
                val verticalSpacing = 24.dp
                val numberOfRows = (smallCards.size + 1) / 2
                val totalHeight = (cardHeight * numberOfRows) + (verticalSpacing * (numberOfRows - 1))

                SmallCardList(
                    modifier =
                        Modifier
                            .padding(horizontal = 24.dp)
                            .height(totalHeight)
                ) {
                    items(smallCards) { cardData ->
                        SmallCard(
                            modifier = Modifier.clickable { onCardClick(cardData.id) },
                            image = painterResource(id = cardData.backgroundRes),
                            selected = cardData.selected,
                            onToggleButton = {},
                            title = cardData.title,
                            fipe = cardData.fipe
                        )
                    }
                }

                if (searchQuery.isEmpty() && recentlyViewedCards.isEmpty()) {
                    Spacer(modifier = Modifier.padding(bottom = 80.dp))
                }

                if (searchQuery.isEmpty() && recentlyViewedCards.isEmpty()) {
                    PrimaryButton(
                        text = "Comparar",
                        onClick = onCompareClick,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }
        }
    }
}
