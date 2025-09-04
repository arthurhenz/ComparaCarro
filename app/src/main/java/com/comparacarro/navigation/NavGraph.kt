package com.comparacarro.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NamedNavArgument
import com.comparacarro.navigation.routes.cardDetailRoute
import com.comparacarro.navigation.routes.homeScreenRoute

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        homeScreenRoute(
            onCardClick = { cardId ->
                navController.navigate(route = Screen.CardDetail.createRoute(cardId))
            }
        )

        cardDetailRoute(goBack = { navController.navigateUp() })
    }
}

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Home : Screen("home")

    object CardDetail : Screen(
        route = "card/{cardId}",
        arguments = listOf(
            navArgument("cardId") {
                type = NavType.StringType
            }
        )
    ) {
        fun createRoute(cardId: String) = "card/$cardId"
    }
}
