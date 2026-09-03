package com.favorites

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.paging.compose.collectAsLazyPagingItems
import com.navigation.EntryProvider
import com.navigation.routes.FavoritesRoute
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.favoriteScreenRoute() {
    entry<FavoritesRoute> {
        val viewModel: FavoriteViewModel = koinViewModel()
        val favorites = viewModel.favorites.collectAsLazyPagingItems()
        val filter by viewModel.filter.collectAsStateWithLifecycle()
        val filterOptions by viewModel.filterOptions.collectAsStateWithLifecycle()
        val favoritesCount by viewModel.favoritesCount.collectAsStateWithLifecycle()

        FavoriteScreen(
            favorites = favorites,
            filter = filter,
            filterOptions = filterOptions,
            skeletonCount = favoritesCount,
            onBrandSelected = viewModel::onBrandSelected,
            onPriceRangeSelected = viewModel::onPriceRangeSelected,
            onYearSelected = viewModel::onYearSelected,
            onClearFilters = viewModel::clearFilters,
            onRemove = { id -> viewModel.onEvent(FavoriteScreenEvent.RemoveFavorite(id)) },
            onCardClick = viewModel::openDetail,
            onCompareClick = viewModel::navigateToCompare,
        )
    }
}

class FavoriteScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { favoriteScreenRoute() }
}
