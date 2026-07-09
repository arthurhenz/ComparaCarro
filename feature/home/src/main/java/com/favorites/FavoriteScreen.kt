package com.favorites

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.theme.Theme
import com.theme.TokenColors
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing
import com.ui.BottomNavBar
import com.ui.BottomNavTab
import com.ui.Header
import com.ui.PrimaryButton
import com.ui.rememberCarImagePainter
import kotlinx.coroutines.flow.flowOf

data class FavoriteCarItem(
    val id: String,
    val brand: String,
    val title: String,
    val price: String,
    val powertrain: String,
    val range: String,
    val imageUrl: String? = null,
)

private data class SuggestionCategory(
    val title: String,
    val description: String,
    val highlighted: Boolean,
)

private val suggestionCategories =
    listOf(
        SuggestionCategory("Performance", "Modelos esportivos em destaque", highlighted = true),
        SuggestionCategory("Utilitários", "SUVs com tração integral", highlighted = false),
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    favorites: LazyPagingItems<FavoriteCarItem>,
    onRemove: (String) -> Unit = {},
    onCardClick: (String) -> Unit = {},
    onCompareClick: () -> Unit = {},
    onNavigate: (BottomNavTab) -> Unit = {},
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearchFocused by rememberSaveable { mutableStateOf(false) }

    // "Empty" only once the initial page has finished loading with no rows — otherwise the empty
    // state would flash during the first Room load.
    val isEmpty = favorites.itemCount == 0 && favorites.loadState.refresh !is LoadState.Loading

    Scaffold(
        containerColor = Theme.colors.background,
        topBar = {
            Header(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearchFocusChanged = { isSearchFocused = it },
                isSearchFocused = isSearchFocused,
                title = "compara carros",
            )
        },
        bottomBar = {
            BottomNavBar(
                selected = BottomNavTab.Favoritos,
                onSelect = onNavigate,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = TokenSpacing.Section),
            verticalArrangement = Arrangement.spacedBy(TokenSpacing.Section),
        ) {
            item {
                FavoritesTitleSection(modifier = Modifier.padding(top = TokenSpacing.Section))
            }

            if (isEmpty) {
                item {
                    EmptyFavoritesState(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = TokenSpacing.Section * 2),
                    )
                }
            } else {
                items(
                    count = favorites.itemCount,
                    key = favorites.itemKey { it.id },
                ) { index ->
                    favorites[index]?.let { car ->
                        FavoriteCard(
                            car = car,
                            onClick = { onCardClick(car.id) },
                            onRemove = { onRemove(car.id) },
                            onCompare = onCompareClick,
                        )
                    }
                }

                item {
                    SuggestionsSection(
                        categories = suggestionCategories,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = TokenSpacing.Section, bottom = TokenSpacing.Section),
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritesTitleSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Favoritos".uppercase(),
            style = Theme.typography.headlineLarge,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Black,
            color = Theme.colors.textPrimary,
        )
        Spacer(
            modifier =
                Modifier
                    .padding(top = TokenSpacing.Item)
                    .height(4.dp)
                    .width(96.dp)
                    .background(Theme.colors.accentPrimary),
        )
        Text(
            text = "Sua garagem particular aguarda".uppercase(),
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary,
            modifier = Modifier.padding(top = TokenSpacing.Block),
        )
    }
}

@Composable
private fun FavoriteCard(
    car: FavoriteCarItem,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    onCompare: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(TokenShapes.Sm)
                .background(Theme.colors.surfaceLow, shape = TokenShapes.Sm)
                .clickable(onClick = onClick)
                .padding(TokenSpacing.Item),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberCarImagePainter(car.imageUrl),
            contentDescription = car.title,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .fillMaxWidth(0.33f)
                    .aspectRatio(16f / 9f)
                    .clip(TokenShapes.Sm)
                    .background(Theme.colors.surfaceRaised, shape = TokenShapes.Sm),
        )
        Spacer(modifier = Modifier.width(TokenSpacing.Block))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = car.brand.uppercase(),
                        style = Theme.typography.labelMedium,
                        color = Theme.colors.accentPrimary,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = car.title.uppercase(),
                        style = Theme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Theme.colors.textPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Remover dos favoritos",
                    tint = Theme.colors.accentPrimary,
                    modifier =
                        Modifier
                            .size(TokenIconSize.Large)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                            ) { onRemove() },
                )
            }
            val hasSpecs = car.range.isNotBlank() || car.powertrain.isNotBlank()
            if (hasSpecs) {
                Spacer(modifier = Modifier.height(TokenSpacing.Item))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (car.range.isNotBlank()) {
                        SpecInline(icon = Icons.Filled.Speed, label = car.range)
                    }
                    if (car.powertrain.isNotBlank()) {
                        SpecInline(icon = powertrainIcon(car.powertrain), label = car.powertrain)
                    }
                }
            }
            Spacer(modifier = Modifier.height(TokenSpacing.Item))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = car.price,
                    style = Theme.typography.priceMedium,
                    fontWeight = FontWeight.Bold,
                    color = Theme.colors.textPrimary,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(TokenSpacing.Item))
                PrimaryButton(
                    text = "Ver Detalhes",
                    onClick = onCompare,
                    expanded = false,
                )
            }
        }
    }
}

private fun powertrainIcon(label: String): ImageVector {
    val lower = label.lowercase()
    return when {
        "turbo" in lower -> Icons.Filled.Bolt
        "esport" in lower || "sport" in lower -> Icons.Filled.Bolt
        "elétric" in lower || "eletric" in lower -> Icons.Filled.Bolt
        else -> Icons.Filled.LocalGasStation
    }
}

@Composable
private fun SpecInline(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Theme.colors.textSecondary,
            modifier = Modifier.size(TokenIconSize.Small),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label.uppercase(),
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary,
        )
    }
}

@Composable
private fun SuggestionsSection(
    categories: List<SuggestionCategory>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "Sugestões baseadas no seu perfil".uppercase(),
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Block))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
        ) {
            categories.forEach { category ->
                SuggestionCard(
                    category = category,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun SuggestionCard(
    category: SuggestionCategory,
    modifier: Modifier = Modifier,
) {
    val borderColor =
        if (category.highlighted) Theme.colors.accentPrimary else TokenColors.Outline
    Row(
        modifier =
            modifier
                .clip(TokenShapes.Sm)
                .background(Theme.colors.surface, shape = TokenShapes.Sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .width(2.dp)
                    .height(48.dp)
                    .background(borderColor),
        )
        Column(
            modifier = Modifier.padding(TokenSpacing.Block),
        ) {
            Text(
                text = category.title.uppercase(),
                style = Theme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Theme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = category.description.uppercase(),
                style = Theme.typography.labelMedium,
                color = Theme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun EmptyFavoritesState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = Theme.colors.textSecondary,
            modifier = Modifier.size(TokenIconSize.ExtraLarge),
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Block))
        Text(
            text = "Você ainda não favoritou nenhum carro",
            style = Theme.typography.titleLarge,
            color = Theme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text = "Toque no coração de um modelo para salvá-lo aqui.",
            style = Theme.typography.bodyMedium,
            color = Theme.colors.textSecondary,
        )
    }
}

internal fun sampleFavorites(): List<FavoriteCarItem> =
    listOf(
        FavoriteCarItem(
            id = "fav_1",
            brand = "Toyota",
            title = "Corolla Altis",
            price = "R$ 187.990",
            powertrain = "Hybrid",
            range = "0 km",
        ),
        FavoriteCarItem(
            id = "fav_2",
            brand = "Fiat",
            title = "Pulse Abarth",
            price = "R$ 149.900",
            powertrain = "Turbo",
            range = "1.200 km",
        ),
        FavoriteCarItem(
            id = "fav_3",
            brand = "Porsche",
            title = "911 Carrera",
            price = "R$ 845.000",
            powertrain = "Sport",
            range = "5.500 km",
        ),
    )

@Preview(showBackground = true)
@Composable
fun FavoriteScreenPreview() {
    Theme {
        val favorites = flowOf(PagingData.from(sampleFavorites())).collectAsLazyPagingItems()
        FavoriteScreen(favorites = favorites)
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteScreenEmptyPreview() {
    Theme {
        val favorites = flowOf(PagingData.from(emptyList<FavoriteCarItem>())).collectAsLazyPagingItems()
        FavoriteScreen(favorites = favorites)
    }
}
