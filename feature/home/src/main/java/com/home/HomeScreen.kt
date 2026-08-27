package com.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.data.model.SmallCardData
import com.theme.Theme
import com.ui.BottomNavBar
import com.ui.BottomNavTab
import com.ui.FullScreenError
import com.ui.Header

@Composable
fun HomeScreen(
    state: HomeScreenState,
    searchQuery: String,
    isSearchFocused: Boolean,
    favoriteIds: Set<String> = emptySet(),
    onCardClick: (String) -> Unit = {},
    onCompareFromHome: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onSearchFocusChanged: (Boolean) -> Unit = {},
    onRefreshRecentlyViewed: () -> Unit = {},
    onToggleFavorite: (SmallCardData) -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    onRefreshRecentlyViewed()
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // The Scaffold lives above the state switch so header and bottom navigation stay
    // visible (and inset-aware) across loading, error and success alike.
    Scaffold(
        containerColor = Theme.colors.background,
        topBar = {
            Column {
                Header(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                    searchQuery = searchQuery,
                    onSearchQueryChange = onSearchQueryChange,
                    onSearchFocusChanged = onSearchFocusChanged,
                    isSearchFocused = isSearchFocused,
                    title = "Compara Carros",
                )

                if (state is HomeScreenState.Success && state.isSearching) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = Theme.colors.accentPrimary,
                    )
                }
            }
        },
        bottomBar = {
            BottomNavBar(
                selected = BottomNavTab.Garagem,
                onSelect = { tab ->
                    when (tab) {
                        BottomNavTab.Garagem -> Unit
                        BottomNavTab.Comparar -> onCompareFromHome()
                        BottomNavTab.Favoritos -> onFavoritesClick()
                        BottomNavTab.Perfil -> onProfileClick()
                    }
                },
            )
        },
    ) { paddingValues ->
        when (state) {
            is HomeScreenState.Loading -> {
                HomeLoadingSkeleton(
                    modifier = Modifier.padding(paddingValues),
                )
            }

            is HomeScreenState.Error -> {
                FullScreenError(
                    message = state.error,
                    modifier = Modifier.padding(paddingValues),
                )
            }

            is HomeScreenState.Success -> {
                HomeScreenContent(
                    modifier = Modifier.padding(paddingValues),
                    smallCards = state.smallCards,
                    searchQuery = searchQuery,
                    listResetToken = state.listResetToken,
                    hasSearchResults = state.hasSearchResults,
                    isSearchFocused = isSearchFocused,
                    onCardClick = onCardClick,
                    favoriteIds = favoriteIds,
                    onToggleFavorite = onToggleFavorite,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val cards =
        listOf(
            SmallCardData(
                id = "preview_small_1",
                title = "Volkswagen Saveiro 2017",
                fipe = "R$ 55.900",
                selected = true,
            ),
            SmallCardData(
                id = "preview_small_2",
                title = "Audi A4 Quattro Sedan 2019",
                fipe = "R$ 142.000",
                selected = false,
            ),
            SmallCardData(
                id = "preview_small_3",
                title = "Honda Civic Si LX LXS 2020",
                fipe = "R$ 115.500",
                selected = false,
            ),
            SmallCardData(
                id = "preview_small_4",
                title = "Toyota Corolla Xei Guerra Corolla Siria 2021",
                fipe = "R$ 128.000",
                selected = true,
            ),
        )
    Theme {
        HomeScreen(
            state = HomeScreenState.Success(smallCards = cards, allSmallCards = cards),
            searchQuery = "",
            isSearchFocused = false,
        )
    }
}
