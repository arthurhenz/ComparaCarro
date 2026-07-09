package com.favorites

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.paging.compose.collectAsLazyPagingItems
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.navigation.EntryProvider
import com.navigation.routes.FavoritesRoute
import com.navigation.routes.HomeScreenRoute
import com.navigation.routes.ProfileRoute
import com.navigation.routes.SelectComparisonRoute
import com.ui.BottomNavTab
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.favoriteScreenRoute() {
    entry<FavoritesRoute> {
        val viewModel: FavoriteViewModel = koinViewModel()
        val favorites = viewModel.favorites.collectAsLazyPagingItems()

        FavoriteScreen(
            favorites = favorites,
            onRemove = { id -> viewModel.onEvent(FavoriteScreenEvent.RemoveFavorite(id)) },
            onCardClick = viewModel::openDetail,
            onCompareClick = viewModel::navigateToCompare,
            onNavigate = viewModel::navigateToTab,
        )
    }
}

fun Navigator.navigateToBottomTab(tab: BottomNavTab) {
    when (tab) {
        BottomNavTab.Garagem -> navigate(HomeScreenRoute, NavOptions(popUpTo = HomeScreenRoute))
        BottomNavTab.Comparar -> navigate(SelectComparisonRoute(null), NavOptions(singleTop = true))
        BottomNavTab.Favoritos -> navigate(FavoritesRoute, NavOptions(singleTop = true))
        BottomNavTab.Perfil -> navigate(ProfileRoute, NavOptions(singleTop = true))
    }
}

class FavoriteScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { favoriteScreenRoute() }
}
