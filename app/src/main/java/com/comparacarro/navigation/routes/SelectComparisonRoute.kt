package com.comparacarro.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.comparacarro.navigation.Screen
import com.selectCompare.SelectComparisonScreen

fun NavGraphBuilder.selectComparisonRoute(
    goBack: () -> Unit,
    onCompareSelected: (firstId: String, secondId: String) -> Unit
) {
    composable(
        route = Screen.SelectComparison.route,
        arguments = Screen.SelectComparison.arguments
    ) { backStackEntry ->
        val maybeFirstId = backStackEntry.arguments?.getString("firstId")
        SelectComparisonScreen(
            firstId = maybeFirstId,
            onBackClick = goBack,
            onCompareClick = { second ->
                val first = maybeFirstId ?: return@SelectComparisonScreen
                onCompareSelected(first, second)
            }
        )
    }
}
