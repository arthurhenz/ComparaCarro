package com.comparacarro.navigation.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.comparacarro.navigation.Screen
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
