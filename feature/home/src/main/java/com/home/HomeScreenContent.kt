package com.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.data.model.LargeCardData
import com.data.model.SmallCardData
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing
import com.ui.BottomNavBar
import com.ui.BottomNavTab
import com.ui.FavoriteButton
import com.ui.Header
import com.ui.LargeCard
import com.ui.LargeCardCarousel
import com.ui.PrimaryButton
import com.ui.SmallCard
import com.ui.SmallCardList

private enum class HomeViewMode { Grid, List }

private val homeCategories = listOf("Todos os Modelos", "SUV", "Sedan", "Hatchback")

@Composable
fun HomeScreenContent(
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
    var viewMode by rememberSaveable { mutableStateOf(HomeViewMode.Grid) }
    var selectedCategory by rememberSaveable { mutableStateOf(homeCategories.first()) }
    var selectedTab by rememberSaveable { mutableStateOf(BottomNavTab.Garagem) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Theme.colors.background)
                .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Header(
            onMenuClick = {},
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearchFocusChanged = onSearchFocusChanged,
            isSearchFocused = isSearchFocused,
            title = "ComparaCarros"
        )

        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 96.dp)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource
                        ) {
                            if (isSearchFocused) {
                                focusManager.clearFocus()
                            }
                        }
            ) {
                if (searchQuery.isEmpty() && recentlyViewedCards.isNotEmpty()) {
                    LargeCardCarousel(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = TokenSpacing.Section)
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
                }

                if (searchQuery.isEmpty()) {
                    Text(
                        text = "Frota Disponível",
                        style = Theme.typography.headlineLarge,
                        color = Theme.colors.textPrimary,
                        modifier =
                            Modifier
                                .padding(horizontal = TokenSpacing.Section)
                                .padding(bottom = TokenSpacing.Item)
                    )

                    CategoryChipRow(
                        categories = homeCategories,
                        selected = selectedCategory,
                        onSelect = { selectedCategory = it },
                        modifier = Modifier.padding(bottom = TokenSpacing.Block)
                    )

                    SortAndViewToggleRow(
                        sortType = sortType,
                        onSortTypeChange = onSortTypeChange,
                        viewMode = viewMode,
                        onViewModeChange = { viewMode = it },
                        modifier =
                            Modifier
                                .padding(horizontal = TokenSpacing.Section)
                                .padding(bottom = TokenSpacing.Block)
                    )
                }

                when (viewMode) {
                    HomeViewMode.Grid -> {
                        val cardHeight = 230.dp
                        val verticalSpacing = TokenSpacing.Section
                        val numberOfRows = (smallCards.size + 1) / 2
                        val totalHeight =
                            (cardHeight * numberOfRows) +
                                (verticalSpacing * (numberOfRows - 1).coerceAtLeast(0))

                        SmallCardList(
                            modifier =
                                Modifier
                                    .padding(horizontal = TokenSpacing.Section)
                                    .height(totalHeight)
                        ) {
                            items(smallCards) { cardData ->
                                SmallCard(
                                    modifier = Modifier.clickable { onCardClick(cardData.id) },
                                    image = painterResource(id = cardData.backgroundRes),
                                    title = cardData.title,
                                    fipe = cardData.fipe,
                                    onClick = { onCardClick(cardData.id) }
                                )
                            }
                        }
                    }

                    HomeViewMode.List -> {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = TokenSpacing.Section),
                            verticalArrangement = Arrangement.spacedBy(TokenSpacing.Block)
                        ) {
                            smallCards.forEach { cardData ->
                                HomeListItem(
                                    image = painterResource(id = cardData.backgroundRes),
                                    title = cardData.title,
                                    fipe = cardData.fipe,
                                    onClick = { onCardClick(cardData.id) }
                                )
                            }
                        }
                    }
                }

                if (searchQuery.isEmpty() && recentlyViewedCards.isEmpty()) {
                    Spacer(modifier = Modifier.height(TokenSpacing.Section))
                }
            }

            PrimaryButton(
                text = "Comparar",
                onClick = onCompareClick,
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = TokenSpacing.Block)
            )
        }

        BottomNavBar(
            selected = selectedTab,
            onSelect = { tab ->
                selectedTab = tab
                if (tab == BottomNavTab.Comparar) onCompareClick()
            }
        )
    }
}

@Composable
private fun CategoryChipRow(
    categories: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = TokenSpacing.Section),
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Item)
    ) {
        categories.forEach { category ->
            CategoryChip(
                label = category,
                selected = category == selected,
                onClick = { onSelect(category) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor =
        if (selected) Theme.colors.surfaceRaised else Theme.colors.surfaceRaised
    val contentColor =
        if (selected) Theme.colors.textInteractive else Theme.colors.textSecondary

    Box(
        modifier =
            Modifier
                .clip(TokenShapes.Pill)
                .background(containerColor, shape = TokenShapes.Pill)
                .clickable(onClick = onClick)
                .padding(horizontal = TokenSpacing.Block, vertical = TokenSpacing.Item),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = Theme.typography.labelMedium,
            color = contentColor
        )
    }
}

@Composable
private fun SortAndViewToggleRow(
    sortType: SortType,
    onSortTypeChange: (SortType) -> Unit,
    viewMode: HomeViewMode,
    onViewModeChange: (HomeViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Text(
                text =
                    when (sortType) {
                        SortType.MOST_POPULAR -> "Mais populares"
                        SortType.ALPHABETIC -> "Alfabética"
                    },
                style = Theme.typography.titleLarge,
                color = Theme.colors.textPrimary,
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

        Row(horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Item)) {
            ViewToggleButton(
                icon = Icons.Filled.GridView,
                contentDescription = "Visualização em grade",
                selected = viewMode == HomeViewMode.Grid,
                onClick = { onViewModeChange(HomeViewMode.Grid) }
            )
            ViewToggleButton(
                icon = Icons.Filled.ViewAgenda,
                contentDescription = "Visualização em lista",
                selected = viewMode == HomeViewMode.List,
                onClick = { onViewModeChange(HomeViewMode.List) }
            )
        }
    }
}

@Composable
private fun ViewToggleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (selected) Theme.colors.accentPrimary else Theme.colors.textSecondary
    Box(
        modifier =
            Modifier
                .size(36.dp)
                .clip(TokenShapes.Sm)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(TokenIconSize.Medium)
        )
    }
}

@Composable
private fun HomeListItem(
    image: Painter,
    title: String,
    fipe: String,
    onClick: () -> Unit
) {
    var favorited by remember { mutableStateOf(false) }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(TokenShapes.Card)
                .background(Theme.colors.surfaceLow, shape = TokenShapes.Card)
                .clickable(onClick = onClick)
                .padding(TokenSpacing.Inline),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier =
                Modifier
                    .size(width = 120.dp, height = 90.dp)
                    .clip(TokenShapes.Sm)
        ) {
            Image(
                painter = image,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.width(TokenSpacing.Block))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = Theme.typography.titleLarge,
                color = Theme.colors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(TokenSpacing.Item / 2))
            Text(
                text = "Tabela Fipe",
                style = Theme.typography.labelMedium,
                color = Theme.colors.textSecondary
            )
            Text(
                text = fipe,
                style = Theme.typography.priceMedium,
                color = Theme.colors.accentPrimary
            )
        }

        FavoriteButton(
            selected = favorited,
            onToggle = { favorited = it }
        )
    }
}
