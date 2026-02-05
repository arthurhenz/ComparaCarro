package com.comparacarro.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.comparacarro.navigation.routes.CardDetailRoute
import com.comparacarro.navigation.routes.CompareScreenRoute
import com.comparacarro.navigation.routes.HomeScreenRoute
import com.comparacarro.navigation.routes.SelectComparisonRoute
import com.comparacarro.navigation.routes.cardDetailRoute
import com.comparacarro.navigation.routes.compareScreenRoute
import com.comparacarro.navigation.routes.homeScreenRoute
import com.comparacarro.navigation.routes.selectComparisonRoute
import com.comparacarro.navigation.utils.EntriesProviderAggregator
import jakarta.inject.Inject
import org.koin.compose.koinInject
import org.koin.java.KoinJavaComponent.inject

@Composable
fun AppNavigation() {

//    val navigationState = rememberNavigationState(
//        startRoute = HomeScreenRoute("home"),
//        topLevelRoutes = setOf(HomeScreenRoute("home"))
//    )

    val entryBuilders = koinInject<EntriesProviderAggregator>().entries

    NavDisplay(
        backStack = rememberNavBackStack(HomeScreenRoute),
        entryProvider = entryProvider {
            entryBuilders.forEach { builder -> this.builder() }
        },
    )
}
