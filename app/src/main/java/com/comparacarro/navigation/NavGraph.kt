package com.comparacarro.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.comparacarro.navigation.routes.CardDetailRoute
import com.comparacarro.navigation.routes.CompareScreenRoute
import com.comparacarro.navigation.routes.HomeScreenRoute
import com.comparacarro.navigation.routes.SelectComparisonRoute
import com.comparacarro.navigation.routes.cardDetailRoute
import com.comparacarro.navigation.routes.compareScreenRoute
import com.comparacarro.navigation.routes.homeScreenRoute
import com.comparacarro.navigation.routes.selectComparisonRoute


@Composable
fun AppNavigation() {
    
    val navigationState = rememberNavigationState(
        startRoute = HomeScreenRoute("home"),
        topLevelRoutes = setOf(HomeScreenRoute("home"))
    )

    val navigator = remember { Navigator(navigationState) }

    val entryProvider = entryProvider {
        homeScreenRoute(
            onCardClick = { cardId ->
                navigator.navigate(CardDetailRoute(cardId))
            },
            onCompareFromHome = {
                navigator.navigate(SelectComparisonRoute(null))
            }
        )

        cardDetailRoute(
            goBack = { navigator.goBack() },
            onCompareFromDetail = { firstId ->
                navigator.navigate(SelectComparisonRoute(firstId))
            }
        )

        selectComparisonRoute(
            goBack = { navigator.goBack() },
            onCompareSelected = { firstId, secondId ->
                navigator.navigate(CompareScreenRoute(firstId, secondId))
            },
            onCardClick = { cardId -> navigator.navigate(CardDetailRoute(cardId)) }
        )

        compareScreenRoute(goBack = { navigator.goBack() })
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() }
    )
}
