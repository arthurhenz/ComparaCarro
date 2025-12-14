package com.comparacarro2.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.comparacarro2.navigation.Screen
import com.comparison.ComparisonScreen

fun NavGraphBuilder.compareScreenRoute(goBack: () -> Unit) {
    composable(
        route = Screen.Compare.route,
        arguments = Screen.Compare.arguments
    ) { backStackEntry ->
        val firstId = backStackEntry.arguments?.getString("firstId") ?: ""
        val secondId = backStackEntry.arguments?.getString("secondId") ?: ""
        ComparisonScreen(
            firstId = firstId,
            secondId = secondId,
            onBackClick = goBack
        )
    }
}
