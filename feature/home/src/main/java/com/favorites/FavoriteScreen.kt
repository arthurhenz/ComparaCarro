package com.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.data.R
import com.theme.Theme
import com.theme.TokenIconSize
import com.theme.TokenShapes
import com.theme.TokenSpacing
import com.ui.BottomNavBar
import com.ui.BottomNavTab
import com.ui.FavoriteButton

data class FavoriteCarItem(
    val id: String,
    val brand: String,
    val title: String,
    val price: String,
    val powertrain: String,
    val range: String,
    val backgroundRes: Int = R.drawable.ic_launcher_background
)

private val suggestionCategories = listOf("Performance", "Utilitários", "Híbridos", "Premium")

@Composable
fun FavoriteScreen(
    initialFavorites: List<FavoriteCarItem> = sampleFavorites(),
    onCardClick: (String) -> Unit = {},
    onCompareClick: () -> Unit = {},
    onNavigate: (BottomNavTab) -> Unit = {}
) {
    val favorites = remember { mutableStateListOf<FavoriteCarItem>().apply { addAll(initialFavorites) } }
    var selectedCategory by rememberSaveable { mutableStateOf(suggestionCategories.first()) }
    var selectedTab by rememberSaveable { mutableStateOf(BottomNavTab.Favoritos) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Theme.colors.background)
                .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        FavoriteHeader()

        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = TokenSpacing.Section)
            ) {
                if (favorites.isEmpty()) {
                    EmptyFavoritesState(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = TokenSpacing.Section * 2)
                    )
                } else {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = TokenSpacing.Section),
                        verticalArrangement = Arrangement.spacedBy(TokenSpacing.Block)
                    ) {
                        favorites.forEach { car ->
                            FavoriteCard(
                                car = car,
                                onClick = { onCardClick(car.id) },
                                onRemove = { favorites.remove(car) },
                                onCompare = onCompareClick
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(TokenSpacing.Section))

                    Text(
                        text = "Sugestões baseadas no seu perfil",
                        style = Theme.typography.headlineLarge,
                        color = Theme.colors.textPrimary,
                        modifier =
                            Modifier
                                .padding(horizontal = TokenSpacing.Section)
                                .padding(bottom = TokenSpacing.Block)
                    )

                    SuggestionChips(
                        categories = suggestionCategories,
                        selected = selectedCategory,
                        onSelect = { selectedCategory = it }
                    )
                }
            }
        }

        BottomNavBar(
            selected = selectedTab,
            onSelect = { tab ->
                selectedTab = tab
                onNavigate(tab)
            }
        )
    }
}

@Composable
private fun FavoriteHeader() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = TokenSpacing.Section, vertical = TokenSpacing.Block)
    ) {
        Text(
            text = "ComparaCarros",
            style = Theme.typography.titleLarge,
            color = Theme.colors.accentPrimary
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text = "FAVORITOS",
            style = Theme.typography.headlineLarge,
            color = Theme.colors.textPrimary
        )
    }
}

@Composable
private fun FavoriteCard(
    car: FavoriteCarItem,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    onCompare: () -> Unit
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(TokenShapes.Card)
                .background(Theme.colors.surfaceLow, shape = TokenShapes.Card)
                .clickable(onClick = onClick)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = car.backgroundRes),
                    contentDescription = car.title,
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                )
                FavoriteButton(
                    selected = true,
                    onToggle = { onRemove() },
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(TokenSpacing.Item)
                )
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(TokenSpacing.Block)
            ) {
                Text(
                    text = car.brand,
                    style = Theme.typography.labelMedium,
                    color = Theme.colors.textSecondary
                )
                Spacer(modifier = Modifier.height(TokenSpacing.Item / 2))
                Text(
                    text = car.title,
                    style = Theme.typography.titleLarge,
                    color = Theme.colors.textPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(TokenSpacing.Item))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Block),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SpecBadge(icon = Icons.Filled.LocalGasStation, label = car.powertrain)
                    SpecBadge(icon = Icons.Filled.Speed, label = car.range)
                }

                Spacer(modifier = Modifier.height(TokenSpacing.Block))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tabela Fipe",
                            style = Theme.typography.labelMedium,
                            color = Theme.colors.textSecondary
                        )
                        Text(
                            text = car.price,
                            style = Theme.typography.priceLarge,
                            color = Theme.colors.accentPrimary
                        )
                    }
                    DetailLinkButton(onClick = onCompare)
                }
            }
        }
    }
}

@Composable
private fun SpecBadge(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Theme.colors.textSecondary,
            modifier = Modifier.size(TokenIconSize.Small)
        )
        Spacer(modifier = Modifier.width(TokenSpacing.Item / 2))
        Text(
            text = label,
            style = Theme.typography.labelMedium,
            color = Theme.colors.textSecondary
        )
    }
}

@Composable
private fun DetailLinkButton(onClick: () -> Unit) {
    Box(
        modifier =
            Modifier
                .clip(TokenShapes.Button)
                .background(Theme.colors.surfaceRaised, shape = TokenShapes.Button)
                .clickable(onClick = onClick)
                .padding(horizontal = TokenSpacing.Block, vertical = TokenSpacing.Item)
    ) {
        Text(
            text = "Ver Detalhes",
            style = Theme.typography.labelMedium,
            color = Theme.colors.accentPrimary
        )
    }
}

@Composable
private fun SuggestionChips(
    categories: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = TokenSpacing.Section),
        horizontalArrangement = Arrangement.spacedBy(TokenSpacing.Item)
    ) {
        categories.forEach { category ->
            val isSelected = category == selected
            Box(
                modifier =
                    Modifier
                        .clip(TokenShapes.Pill)
                        .background(
                            if (isSelected) Theme.colors.accentPrimary else Theme.colors.surfaceRaised,
                            shape = TokenShapes.Pill
                        )
                        .clickable { onSelect(category) }
                        .padding(horizontal = TokenSpacing.Block, vertical = TokenSpacing.Item)
            ) {
                Text(
                    text = category,
                    style = Theme.typography.labelMedium,
                    color = if (isSelected) Theme.colors.textInteractive else Theme.colors.textSecondary
                )
            }
        }
    }
}

@Composable
private fun EmptyFavoritesState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FavoriteButton(selected = false, onToggle = {})
        Spacer(modifier = Modifier.height(TokenSpacing.Block))
        Text(
            text = "Você ainda não favoritou nenhum carro",
            style = Theme.typography.titleLarge,
            color = Theme.colors.textPrimary
        )
        Spacer(modifier = Modifier.height(TokenSpacing.Item))
        Text(
            text = "Toque no coração de um modelo para salvá-lo aqui.",
            style = Theme.typography.bodyMedium,
            color = Theme.colors.textSecondary
        )
    }
}

internal fun sampleFavorites(): List<FavoriteCarItem> = listOf(
    FavoriteCarItem(
        id = "fav_1",
        brand = "Toyota",
        title = "Corolla Altis Híbrido",
        price = "R$ 187.990",
        powertrain = "Híbrido / Flex",
        range = "1.200 km"
    ),
    FavoriteCarItem(
        id = "fav_2",
        brand = "Fiat",
        title = "Pulse Abarth Turbo",
        price = "R$ 149.900",
        powertrain = "Flex-Fuel Turbo",
        range = "780 km"
    ),
    FavoriteCarItem(
        id = "fav_3",
        brand = "Porsche",
        title = "911 Carrera Sport",
        price = "R$ 845.000",
        powertrain = "Gasolina Sport",
        range = "650 km"
    )
)

@Preview(showBackground = true)
@Composable
fun FavoriteScreenPreview() {
    Theme {
        FavoriteScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteScreenEmptyPreview() {
    Theme {
        FavoriteScreen(initialFavorites = emptyList())
    }
}
