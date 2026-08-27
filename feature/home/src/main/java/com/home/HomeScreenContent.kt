package com.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.data.model.SmallCardData
import com.theme.SpaceGroteskFamily
import com.theme.Theme
import com.theme.TokenColors
import com.theme.TokenFontSizes
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing
import com.ui.LargeCardList
import com.ui.SmallCard
import com.ui.SmallCardSkeleton
import com.ui.rememberCarImagePainter

private enum class HomeViewMode { Grid, List }

private val homeCategories = listOf("Todos os Modelos", "SUV", "Sedan", "Hatchback")

private const val SKELETON_CARD_COUNT = 6

/**
 * Loading-state body of the Home screen. The static chrome (hero title, category chips, sort row)
 * renders exactly as in the success grid; only the card slots show shimmering [SmallCardSkeleton]s.
 */
@Composable
fun HomeLoadingSkeleton(
    modifier: Modifier = Modifier,
    sortType: SortType = SortType.MOST_POPULAR,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        userScrollEnabled = false,
        contentPadding =
            PaddingValues(
                start = TokenSpacing.Item,
                end = TokenSpacing.Item,
                bottom = TokenSpacing.Section,
            ),
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
        verticalArrangement = Arrangement.spacedBy(TokenSpacing.Inline),
    ) {
        item(key = "hero", span = { GridItemSpan(maxLineSpan) }, contentType = "hero") {
            HeroTitle(modifier = Modifier.padding(start = TokenSpacing.Item, top = 24.dp))
        }

        item(key = "categories", span = { GridItemSpan(maxLineSpan) }, contentType = "categories") {
//            CategoryChipRow(
//                categories = homeCategories,
//                selected = homeCategories.first(),
//                onSelect = {},
//                modifier = Modifier.padding(top = 28.dp),
//            )
        }

        item(key = "sort", span = { GridItemSpan(maxLineSpan) }, contentType = "sort") {
            SortAndViewToggleRow(
                sortType = sortType,
                onSortTypeChange = {},
                viewMode = HomeViewMode.Grid,
                onViewModeChange = {},
                modifier = Modifier.padding(horizontal = TokenSpacing.Block),
            )
        }

        items(SKELETON_CARD_COUNT) {
            SmallCardSkeleton()
        }
    }
}

/**
 * Success-state body of the Home screen: a single [LazyVerticalGrid] that owns the scrolling.
 * The hero title, category chips and sort row are full-span items so everything scrolls
 * together while cards still lay out (and compose lazily) two per row.
 */
@Composable
fun HomeScreenContent(
    smallCards: List<SmallCardData>,
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchQuery: String = "",
    listResetToken: Int = 0,
    isSearchFocused: Boolean = false,
    sortType: SortType = SortType.MOST_POPULAR,
    onSortTypeChange: (SortType) -> Unit = {},
    favoriteIds: Set<String> = emptySet(),
    onToggleFavorite: (SmallCardData) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    var viewMode by rememberSaveable { mutableStateOf(HomeViewMode.Grid) }
    var selectedCategory by rememberSaveable { mutableStateOf(homeCategories.first()) }
    val gridState = rememberLazyGridState()

    // When a new set of results lands (search / browse restore), jump back to the top.
    LaunchedEffect(listResetToken) {
        gridState.animateScrollToItem(0)
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        modifier =
            modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                ) {
                    if (isSearchFocused) {
                        focusManager.clearFocus()
                    }
                },
        contentPadding =
            PaddingValues(
                start = TokenSpacing.Item,
                end = TokenSpacing.Item,
                bottom = TokenSpacing.Section,
            ),
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
        verticalArrangement = Arrangement.spacedBy(TokenSpacing.Inline),
    ) {
        if (searchQuery.isEmpty()) {
            item(key = "hero", span = { GridItemSpan(maxLineSpan) }, contentType = "hero") {
                HeroTitle(modifier = Modifier.padding(start = TokenSpacing.Item, top = 24.dp))
            }

            item(key = "categories", span = { GridItemSpan(maxLineSpan) }, contentType = "categories") {
                // The grid's verticalArrangement already adds Inline (12dp) between lines;
                // this only tops it up to the original 40dp gap below the hero title.
//                CategoryChipRow(
//                    categories = homeCategories,
//                    selected = selectedCategory,
//                    onSelect = { selectedCategory = it },
//                    modifier = Modifier.padding(top = 28.dp),
//                )
            }

            item(key = "sort", span = { GridItemSpan(maxLineSpan) }, contentType = "sort") {
                SortAndViewToggleRow(
                    sortType = sortType,
                    onSortTypeChange = onSortTypeChange,
                    viewMode = viewMode,
                    onViewModeChange = { viewMode = it },
                    modifier = Modifier.padding(horizontal = TokenSpacing.Block),
                )
            }
        }

        when (viewMode) {
            HomeViewMode.Grid -> {
                items(smallCards, key = { it.id }, contentType = { "card" }) { cardData ->
                    SmallCard(
                        image = rememberCarImagePainter(cardData.imageUrl),
                        brand = cardData.title.substringBefore(" "),
                        model = cardData.title.substringAfter(" ", missingDelimiterValue = ""),
                        fipe = cardData.fipe,
                        onClick = { onCardClick(cardData.id) },
                    )
                }
            }

            HomeViewMode.List -> {
                items(
                    smallCards,
                    key = { it.id },
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = { "row" },
                ) { cardData ->
                    LargeCardList(
                        image = rememberCarImagePainter(cardData.imageUrl),
                        brand = cardData.title.substringBefore(" "),
                        model = cardData.title.substringAfter(" ", missingDelimiterValue = ""),
                        fipe = cardData.fipe,
                        onClick = { onCardClick(cardData.id) },
                        favorited = cardData.id in favoriteIds,
                        onFavoriteToggle = { onToggleFavorite(cardData) },
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroTitle(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Frota Disponível".uppercase(),
            style =
                TextStyle(
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = TokenFontSizes.Medium,
                ),
            color = Theme.colors.accentPrimary,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        Text(
            text = "Máquinas".uppercase(),
            style = Theme.typography.headlineLarge,
            fontStyle = FontStyle.Italic,
            fontSize = 56.sp,
            color = Theme.colors.textPrimary,
        )

        Spacer(Modifier.height(4.dp).width(156.dp).background(color = TokenColors.PrimaryAccent))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryChipRow(
    categories: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = TokenSpacing.Block),
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Item),
        verticalArrangement = Arrangement.spacedBy(TokenSpacing.Item),
    ) {
        categories.forEach { category ->
            CategoryChip(
                label = category,
                selected = category == selected,
                onClick = { onSelect(category) },
            )
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val contentColor =
        if (selected) Theme.colors.textPrimary else Theme.colors.textSecondary

    Box(
        modifier =
            Modifier
                .clip(TokenShapes.Pill)
                .background(Theme.colors.surfaceRaised, shape = TokenShapes.Pill)
                .clickable(onClick = onClick)
                .padding(horizontal = TokenSpacing.Block)
                .height(TokenSpacing.Section),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label.uppercase(),
            style = Theme.typography.labelMedium,
            maxLines = 1,
            color = contentColor,
        )
    }
}

@Composable
private fun SortAndViewToggleRow(
    sortType: SortType,
    onSortTypeChange: (SortType) -> Unit,
    viewMode: HomeViewMode,
    onViewModeChange: (HomeViewMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Inline),
                modifier =
                    Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) { dropdownExpanded = true },
            ) {
                Text(
                    text =
                        when (sortType) {
                            SortType.MOST_POPULAR -> "Mais populares"
                            SortType.ALPHABETIC -> "Alfabética"
                        },
                    style = Theme.typography.titleLarge,
                    color = Theme.colors.textPrimary,
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Ordenar",
                    tint = Theme.colors.textPrimary,
                    modifier =
                        Modifier
                            .size(TokenIconSize.Medium)
                            .rotate(if (dropdownExpanded) 180f else 0f),
                )
            }
            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text("Mais populares") },
                    onClick = {
                        onSortTypeChange(SortType.MOST_POPULAR)
                        dropdownExpanded = false
                    },
                )
                DropdownMenuItem(
                    text = { Text("Alfabética") },
                    onClick = {
                        onSortTypeChange(SortType.ALPHABETIC)
                        dropdownExpanded = false
                    },
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Item)) {
            ViewToggleButton(
                icon = Icons.Filled.GridView,
                contentDescription = "Visualização em grade",
                selected = viewMode == HomeViewMode.Grid,
                onClick = { onViewModeChange(HomeViewMode.Grid) },
            )
            ViewToggleButton(
                icon = Icons.Filled.ViewAgenda,
                contentDescription = "Visualização em lista",
                selected = viewMode == HomeViewMode.List,
                onClick = { onViewModeChange(HomeViewMode.List) },
            )
        }
    }
}

@Composable
private fun ViewToggleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (selected) Theme.colors.accentPrimary else Theme.colors.textSecondary
    Box(
        modifier =
            Modifier
                .size(36.dp)
                .clip(TokenShapes.Sm)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(TokenIconSize.Medium),
        )
    }
}
