package com.selectCompare

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.common.navigation.NavOptions
import com.navigation.EntryProvider
import com.navigation.routes.CompareScreenRoute
import com.navigation.routes.HomeScreenRoute
import com.navigation.routes.SelectComparisonRoute
import com.navigation.routes.navigateToDetail
import com.navigation.routes.parseVehicleSpec
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun EntryProviderScope<NavKey>.selectComparisonRoute() {
    entry<SelectComparisonRoute> { key ->
        val viewModel: SelectComparisonViewModel = koinViewModel { parametersOf(key.firstId ?: "") }

        val state by viewModel.state.collectAsState()

        SelectComparisonScreen(
            state = state,
            onBackClick = viewModel::goBack,
            onCardClick = viewModel::navigateToDetail,
            onCompareClick = { pair ->
                val (firstSlug, firstFuel, firstYear) = parseVehicleSpec(pair.first)
                val (secondSlug, secondFuel, secondYear) = parseVehicleSpec(pair.second)
                viewModel.navigate(
                    CompareScreenRoute(
                        firstModelSlug = firstSlug,
                        firstFuelAcronym = firstFuel,
                        firstYear = firstYear,
                        secondModelSlug = secondSlug,
                        secondFuelAcronym = secondFuel,
                        secondYear = secondYear,
                    ),
                    NavOptions(popUpTo = HomeScreenRoute),
                )
            },
            onSearchQueryChange = viewModel::updateSearchQuery,
            onSearchFocusChanged = viewModel::updateSearchFocus,
            onToggleSelect = viewModel::toggleSelection,
            onLoadMore = viewModel::loadNextPageIfNeeded,
            onNavigateToTab = viewModel::navigateToBottomTab,
        )
    }
}

class SelectComparisonScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { selectComparisonRoute() }
}
