package com.comparacarro2.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.comparacarro2.navigation.Screen
import com.detail.DetailScreen

fun NavGraphBuilder.cardDetailRoute(
    goBack: () -> Unit,
    onCompareFromDetail: (String) -> Unit
) {
    composable(
        route = Screen.CardDetail.route,
        arguments = Screen.CardDetail.arguments
    ) { backStackEntry ->
        val cardId = backStackEntry.arguments?.getString("cardId") ?: ""
        DetailScreen(
            cardId = cardId,
            onBackClick = goBack,
            onCompareClick = { firstId -> onCompareFromDetail(firstId) }
        )
    }
}
