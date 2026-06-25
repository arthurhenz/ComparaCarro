package com.selectCompare

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.data.model.SmallCardData
import com.theme.SpaceGroteskFamily
import com.theme.Theme
import com.theme.TokenColors
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing
import com.ui.SearchField
import com.ui.rememberCarImagePainter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectComparisonContent(
    modifier: Modifier = Modifier,
    state: SelectComparisonScreenState.Success,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    onToggleSelect: (String) -> Unit,
    onCardClick: (String) -> Unit,
    onCompareClick: (Pair<String, String>) -> Unit,
    onLoadMore: (Int) -> Unit,
) {
    val selectedIds = state.selectedCards.map { it.id }
    val isCompareEnabled = state.selectedCards.size == 2

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(visible = state.isSearchFocused) {
                SearchField(
                    searchQuery = state.searchQuery,
                    onSearchQueryChange = onSearchQueryChange,
                    onSearchFocusChanged = onSearchFocusChanged,
                    isSearchFocused = state.isSearchFocused,
                )
            }

            if (state.isSearching) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Theme.colors.accentPrimary,
                )
            }

            val listState = rememberLazyListState()
            LaunchedEffect(listState.firstVisibleItemIndex, state.smallCards.size) {
                val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                onLoadMore(lastVisibleIndex)
            }

            // When a new set of results lands (search / browse restore), jump back to the top.
            LaunchedEffect(state.listResetToken) {
                listState.animateScrollToItem(0)
            }

            LazyColumn(
                state = listState,
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = TokenSpacing.Section),
                contentPadding =
                    PaddingValues(
                        top = TokenSpacing.Section,
                        bottom = 100.dp,
                    ),
                verticalArrangement = Arrangement.spacedBy(TokenSpacing.Item),
            ) {
                item { SelectionHeader() }

                stickyHeader { SelectionCounter(selectedCount = selectedIds.size) }

                // Selected cars stay pinned at the top, even when filtered out of the current results.
                val selectedIdSet = selectedIds.toSet()
                val unselectedCards = state.smallCards.filterNot { selectedIdSet.contains(it.id) }
                val orderedCards = state.selectedCards + unselectedCards

                itemsIndexed(orderedCards, key = { _, card -> card.id }) { _, card ->
                    CarSelectItem(
                        card = card,
                        onToggleSelect = { onToggleSelect(card.id) },
                        onClick = { onCardClick(card.id) },
                    )
                }

                if (state.isLoadingMore) {
                    item {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = TokenSpacing.Block),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = Theme.colors.accentTertiary)
                        }
                    }
                }
            }
        }

        CompareNowButton(
            enabled = isCompareEnabled,
            onClick = {
                if (isCompareEnabled) {
                    onCompareClick(Pair(selectedIds[0], selectedIds[1]))
                }
            },
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = TokenSpacing.Section, vertical = TokenSpacing.Block),
        )
    }
}

@Composable
private fun SelectionHeader() {
    Column(modifier = Modifier.padding(bottom = TokenSpacing.Block)) {
        Text(
            text = "Comparação Técnica".uppercase(),
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text = "Selecione 2 máquinas para comparar",
            style = Theme.typography.headlineLarge,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.ExtraBold,
            color = Theme.colors.textPrimary,
        )
    }
}

@Composable
private fun SelectionCounter(selectedCount: Int) {
    val statusColor =
        if (selectedCount == 2) Theme.colors.accentPrimary else TokenColors.Outline
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Theme.colors.surfaceGlass)
                .padding(TokenSpacing.Block),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$selectedCount/2",
                style = Theme.typography.titleLarge,
                fontFamily = SpaceGroteskFamily,
                fontWeight = FontWeight.Bold,
                color = Theme.colors.accentPrimary,
            )
            Spacer(modifier = Modifier.width(TokenSpacing.Item))
            Text(
                text = "Selecionados".uppercase(),
                style = Theme.typography.labelMedium,
                color = Theme.colors.textSecondary,
            )
        }
        Box(
            modifier =
                Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(statusColor),
        )
    }
}

@Composable
private fun CarSelectItem(
    card: SmallCardData,
    onToggleSelect: () -> Unit,
    onClick: () -> Unit,
) {
    val background =
        if (card.selected) Theme.colors.surface else Theme.colors.surfaceLow
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(TokenShapes.Sm)
                .background(background, shape = TokenShapes.Sm)
                .clickable(onClick = onClick)
                .padding(TokenSpacing.Block),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (card.selected) {
            Box(
                modifier =
                    Modifier
                        .width(4.dp)
                        .height(64.dp)
                        .background(Theme.colors.accentPrimary),
            )
            Spacer(modifier = Modifier.width(TokenSpacing.Inline))
        }
        Image(
            painter = rememberCarImagePainter(card.imageUrl),
            contentDescription = card.title,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(width = 96.dp, height = 64.dp)
                    .clip(TokenShapes.Sm)
                    .background(Theme.colors.surfaceRaised, shape = TokenShapes.Sm),
        )
        Spacer(modifier = Modifier.width(TokenSpacing.Block))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = card.title.uppercase(),
                style = Theme.typography.titleLarge,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Black,
                color =
                    if (card.selected) Theme.colors.accentPrimary else Theme.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = card.fipe,
                style = Theme.typography.labelMedium,
                color = Theme.colors.textSecondary,
            )
        }
        Spacer(modifier = Modifier.width(TokenSpacing.Inline))
        SelectionCheckbox(
            selected = card.selected,
            onClick = onToggleSelect,
        )
    }
}

@Composable
private fun SelectionCheckbox(selected: Boolean, onClick: () -> Unit) {
    val borderColor =
        if (selected) Theme.colors.accentPrimary else TokenColors.Outline
    val fillColor =
        if (selected) Theme.colors.accentPrimary else Color.Transparent
    Box(
        modifier =
            Modifier
                .size(24.dp)
                .clip(TokenShapes.Sm)
                .background(fillColor, shape = TokenShapes.Sm)
                .border(width = 2.dp, color = borderColor, shape = TokenShapes.Sm)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Selecionado",
                tint = TokenColors.OnPrimaryFixed,
                modifier = Modifier.size(TokenIconSize.Small),
            )
        }
    }
}

@Composable
private fun CompareNowButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor =
        if (enabled) Theme.colors.accentPrimary else TokenColors.Outline
    val textColor =
        if (enabled) TokenColors.OnPrimaryFixed else Theme.colors.textPrimary
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(TokenShapes.Sm)
                .background(backgroundColor, shape = TokenShapes.Sm)
                .clickable(enabled = enabled, onClick = onClick)
                .padding(vertical = TokenSpacing.Block),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Comparar agora".uppercase(),
            style = Theme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = textColor,
        )
    }
}
