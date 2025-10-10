package com.comparacarro.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.comparacarro.navigation.routes.cardDetailRoute
import com.comparacarro.navigation.routes.compareScreenRoute
import com.comparacarro.navigation.routes.homeScreenRoute
import com.comparacarro.navigation.routes.selectComparisonRoute

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
            },
            onCompareFromHome = {
                navController.navigate(Screen.SelectComparison.createRoute(null))
            }
        )

        cardDetailRoute(
            goBack = { navController.navigateUp() },
            onCompareFromDetail = { firstId ->
                navController.navigate(Screen.SelectComparison.createRoute(firstId))
            }
        )

        selectComparisonRoute(
            goBack = { navController.navigateUp() },
            onCompareSelected = { firstId, secondId ->
                navController.navigate(Screen.Compare.createRoute(firstId, secondId))
            },
            onCardClick = { cardId -> navController.navigate(route = Screen.CardDetail.createRoute(cardId)) }
        )

        compareScreenRoute(goBack = { navController.navigateUp() })
    }
}

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Home : Screen("home")

    object Compare : Screen(
        route = "comparison/{firstId}/{secondId}",
        arguments =
            listOf(
                navArgument("firstId") { type = NavType.StringType },
                navArgument("secondId") { type = NavType.StringType }
            )
    ) {
        fun createRoute(
            firstId: String,
            secondId: String
        ) = "comparison/$firstId/$secondId"
    }

    object SelectComparison : Screen(
        route = "select_comparison?firstId={firstId}",
        arguments =
            listOf(
                navArgument("firstId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
    ) {
        fun createRoute(firstId: String?) =
            if (firstId.isNullOrBlank()) "select_comparison" else "select_comparison?firstId=$firstId"
    }

    object CardDetail : Screen(
        route = "card/{cardId}",
        arguments =
            listOf(
                navArgument("cardId") {
                    type = NavType.StringType
                }
            )
    ) {
        fun createRoute(cardId: String) = "card/$cardId"
    }
}
