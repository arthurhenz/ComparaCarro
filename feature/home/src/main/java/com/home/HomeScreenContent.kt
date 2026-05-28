package com.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.data.model.LargeCardData
import com.data.model.SmallCardData
import com.theme.SpaceGroteskFamily
import com.theme.Theme
import com.theme.TokenColors
import com.theme.TokenFontSizes
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing
import com.ui.BottomNavBar
import com.ui.BottomNavTab
import com.ui.FavoriteButton
import com.ui.Header
import com.ui.LargeCardList
import com.ui.R as UiR
import com.ui.SmallCard
import com.ui.SmallCardList

private enum class HomeViewMode { Grid, List }

private val homeCategories = listOf("Todos os Modelos", "SUV", "Sedan", "Hatchback")

private val hardcodedCarImages = listOf(
    UiR.drawable.car1,
    UiR.drawable.car2,
    UiR.drawable.car3jpg,
)

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
    onCompareClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    var viewMode by rememberSaveable { mutableStateOf(HomeViewMode.Grid) }
    var selectedCategory by rememberSaveable { mutableStateOf(homeCategories.first()) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Theme.colors.background)
                .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Header(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearchFocusChanged = onSearchFocusChanged,
            isSearchFocused = isSearchFocused,
            title = "Compara Carros",
        )

        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                        ) {
                            if (isSearchFocused) {
                                focusManager.clearFocus()
                            }
                        },
            ) {
//                if (searchQuery.isEmpty() && recentlyViewedCards.isNotEmpty()) {
//                    LargeCardCarousel(
//                        modifier =
//                            Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = TokenSpacing.Section),
//                    ) {
//                        recentlyViewedCards.forEach { cardData ->
//                            item {
//                                LargeCard(
//                                    modifier = Modifier.clickable { onCardClick(cardData.id) },
//                                    background = painterResource(id = cardData.backgroundRes),
//                                    title = cardData.title,
//                                )
//                            }
//                        }
//                    }
//                }

                if (searchQuery.isEmpty()) {
                    Column(modifier = Modifier.padding(start = TokenSpacing.Block, top = 48.dp)) {
                        Text(
                            text = "Frota Disponível".uppercase(),
                            style = TextStyle(
                                fontFamily = SpaceGroteskFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = TokenFontSizes.Medium,
                            ),
                            color = Theme.colors.accentPrimary,
                            modifier =
                                Modifier.padding(bottom = 4.dp)

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

                    CategoryChipRow(
                        categories = homeCategories,
                        selected = selectedCategory,
                        onSelect = { selectedCategory = it },
                        modifier = Modifier.padding(bottom = TokenSpacing.Block).padding(top = 40.dp),
                    )

                    SortAndViewToggleRow(
                        sortType = sortType,
                        onSortTypeChange = onSortTypeChange,
                        viewMode = viewMode,
                        onViewModeChange = { viewMode = it },
                        modifier =
                            Modifier
                                .padding(horizontal = TokenSpacing.Section)
                                .padding(bottom = TokenSpacing.Block),
                    )
                }

                when (viewMode) {
                    HomeViewMode.Grid -> {
                        val cardHeight = 230.dp
                        val verticalSpacing = TokenSpacing.Item
                        val numberOfRows = (smallCards.size + 1) / 2
                        val totalHeight =
                            (cardHeight * numberOfRows) +
                                    (verticalSpacing * (numberOfRows - 1).coerceAtLeast(0))

                        SmallCardList(
                            modifier =
                                Modifier
                                    .padding(horizontal = TokenSpacing.Item)
                                    .height(totalHeight),
                        ) {
                            itemsIndexed(smallCards) { index, cardData ->
                                SmallCard(
                                    modifier = Modifier.clickable { onCardClick(cardData.id) },
                                    image = painterResource(
                                        id = hardcodedCarImages[index % hardcodedCarImages.size],
                                    ),
                                    brand = cardData.title.substringBefore(" "),
                                    model = cardData.title.substringAfter(" ", missingDelimiterValue = ""),
                                    fipe = cardData.fipe,
                                    onClick = { onCardClick(cardData.id) },
                                )
                            }
                        }
                    }

                    HomeViewMode.List -> {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = TokenSpacing.Item),
                            verticalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
                        ) {
                            smallCards.forEachIndexed { index, cardData ->
                                LargeCardList(
                                    image = painterResource(
                                        id = hardcodedCarImages[index % hardcodedCarImages.size],
                                    ),
                                    brand = cardData.title.substringBefore(" "),
                                    model = cardData.title.substringAfter(" ", missingDelimiterValue = ""),
                                    fipe = cardData.fipe,
                                    onClick = { onCardClick(cardData.id) },
                                )
                            }
                        }
                    }
                }

                if (searchQuery.isEmpty() && recentlyViewedCards.isEmpty()) {
                    Spacer(modifier = Modifier.height(TokenSpacing.Section))
                }
            }
        }

        BottomNavBar(
            selected = BottomNavTab.Garagem,
            onSelect = { tab ->
                when (tab) {
                    BottomNavTab.Garagem -> Unit
                    BottomNavTab.Comparar -> onCompareClick()
                    BottomNavTab.Favoritos -> onFavoritesClick()
                    BottomNavTab.Perfil -> onProfileClick()
                }
            },
        )
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
                .padding(horizontal = TokenSpacing.Section),
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
    val containerColor =
        if (selected) Theme.colors.surfaceRaised else Theme.colors.surfaceRaised
    val contentColor =
        if (selected) Theme.colors.textPrimary else Theme.colors.textSecondary

    Box(
        modifier =
            Modifier
                .clip(TokenShapes.Pill)
                .background(containerColor, shape = TokenShapes.Pill)
                .clickable(onClick = onClick)
                .padding(horizontal = TokenSpacing.Block).height(TokenSpacing.Section),
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
            Text(
                text =
                    when (sortType) {
                        SortType.MOST_POPULAR -> "Mais populares"
                        SortType.ALPHABETIC -> "Alfabética"
                    },
                style = Theme.typography.titleLarge,
                color = Theme.colors.textPrimary,
                modifier = Modifier.clickable { dropdownExpanded = true },
            )
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
