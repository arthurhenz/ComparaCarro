package com.home

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.navigation.EntryProvider
import com.navigation.routes.HomeScreenRoute
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.homeScreenRoute() {
    entry<HomeScreenRoute> { key ->
        // Tambem podemos ter Composable como HomeScreenRoute
        val viewModel: HomeViewModel = koinViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()
        val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
        val isSearchFocused by viewModel.isSearchFocused.collectAsStateWithLifecycle()
        val sortType by viewModel.sortType.collectAsStateWithLifecycle()
        val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()

        HomeScreen(
            state = state,
            searchQuery = searchQuery,
            isSearchFocused = isSearchFocused,
            sortType = sortType,
            favoriteIds = favoriteIds,
            onCardClick = viewModel::navigateToDetail,
            onCompareFromHome = viewModel::navigateToSelectComparison,
            onFavoritesClick = viewModel::navigateToFavorites,
            onProfileClick = viewModel::navigateToProfile,
            onSearchQueryChange = viewModel::updateSearchQuery,
            onSearchFocusChanged = viewModel::updateSearchFocus,
            onSortTypeChange = viewModel::updateSortType,
            onRefreshRecentlyViewed = viewModel::refreshRecentlyViewed,
            onToggleFavorite = viewModel::toggleFavorite,
        )
    }
}

class HomeScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { homeScreenRoute() }
}
