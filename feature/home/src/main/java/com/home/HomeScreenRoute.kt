package com.home

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.navigation.EntryProvider
import com.navigation.routes.HomeScreenRoute
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.homeScreenRoute() {
    entry<HomeScreenRoute> { key ->
        // Tambem podemos ter Composable como HomeScreenRoute
        val viewModel: HomeViewModel = koinViewModel()

        val state by viewModel.state.collectAsState()
        val searchQuery by viewModel.searchQuery.collectAsState()
        val isSearchFocused by viewModel.isSearchFocused.collectAsState()
        val sortType by viewModel.sortType.collectAsState()

        HomeScreen(
            state = state,
            searchQuery = searchQuery,
            isSearchFocused = isSearchFocused,
            sortType = sortType,
            onCardClick = viewModel::navigateToDetail,
            onCompareFromHome = viewModel::navigateToSelectComparison,
            onSearchQueryChange = viewModel::updateSearchQuery,
            onSearchFocusChanged = viewModel::updateSearchFocus,
            onSortTypeChange = viewModel::updateSortType,
            onRefreshRecentlyViewed = viewModel::refreshRecentlyViewed
        )
    }
}

class HomeScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { homeScreenRoute() }
}
