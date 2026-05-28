package com.favorites

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.navigation.EntryProvider
import com.navigation.routes.CardDetailRoute
import com.navigation.routes.FavoritesRoute
import com.navigation.routes.HomeScreenRoute
import com.navigation.routes.ProfileRoute
import com.navigation.routes.SelectComparisonRoute
import com.ui.BottomNavTab
import org.koin.compose.koinInject

fun EntryProviderScope<NavKey>.favoriteScreenRoute() {
    entry<FavoritesRoute> {
        val navigator = koinInject<Navigator>()
        FavoriteScreen(
            onCardClick = { id ->
                navigator.navigate(CardDetailRoute(id), NavOptions(singleTop = true))
            },
            onCompareClick = {
                navigator.navigate(SelectComparisonRoute(null), NavOptions(singleTop = true))
            },
            onNavigate = { tab -> navigator.navigateToBottomTab(tab) },
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
