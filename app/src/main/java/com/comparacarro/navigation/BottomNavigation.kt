package com.comparacarro.navigation

import androidx.navigation3.runtime.NavKey
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.navigation.routes.CompareScreenRoute
import com.navigation.routes.FavoritesRoute
import com.navigation.routes.HomeScreenRoute
import com.navigation.routes.LoginRoute
import com.navigation.routes.ProfileRoute
import com.navigation.routes.SelectComparisonRoute
import com.ui.BottomNavTab

/**
 * Which bottom-navigation tab a route belongs to, or `null` when the route is a full-screen
 * destination (detail, signup, forgot password) that hides the bar entirely.
 */
fun NavKey.bottomNavTab(): BottomNavTab? =
    when (this) {
        HomeScreenRoute -> BottomNavTab.Garagem
        is SelectComparisonRoute, is CompareScreenRoute -> BottomNavTab.Comparar
        FavoritesRoute -> BottomNavTab.Favoritos
        ProfileRoute, LoginRoute -> BottomNavTab.Perfil
        else -> null
    }

/** Single source of truth for what tapping a bottom tab does, shared by every screen. */
fun Navigator.navigateToBottomTab(tab: BottomNavTab) {
    when (tab) {
        BottomNavTab.Garagem -> navigate(HomeScreenRoute, NavOptions(popUpTo = HomeScreenRoute, singleTop = true))
        BottomNavTab.Comparar -> navigate(SelectComparisonRoute(null), NavOptions(singleTop = true))
        BottomNavTab.Favoritos -> navigate(FavoritesRoute, NavOptions(singleTop = true))
        BottomNavTab.Perfil -> navigate(ProfileRoute, NavOptions(singleTop = true))
    }
}
