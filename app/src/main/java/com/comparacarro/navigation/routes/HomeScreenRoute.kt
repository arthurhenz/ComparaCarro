package com.comparacarro2.navigation.routes

import androidx.compose.runtime.Composable
import com.comparacarro2.navigation.Screen
import com.home.HomeScreen

fun NavGraphBuilder.homeScreenRoute(
    onCardClick: (String) -> Unit,
    onCompareFromHome: () -> Unit
) {
    composable(
        route = Screen.Home.route
    ) {
        HomeScreenRoute(
            onCardClick = onCardClick,
            onCompareFromHome = onCompareFromHome
        )
    }
}

@Composable
private fun HomeScreenRoute(
    onCardClick: (String) -> Unit,
    onCompareFromHome: () -> Unit
) {
    HomeScreen(
        onCardClick = onCardClick,
        onCompareFromHome = onCompareFromHome
    )
}
