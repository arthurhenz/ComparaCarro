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
            onCompareClick = { viewModel.navigate(
                CompareScreenRoute(it.first, it.second),
                NavOptions(popUpTo = HomeScreenRoute)
            ) }, // USAR  EXTENSION FUNCTION
            onSearchQueryChange = viewModel::updateSearchQuery,
            onSearchFocusChanged = viewModel::updateSearchFocus,
            onToggleSelect = viewModel::toggleSelection,
            onLoadMore = viewModel::loadNextPageIfNeeded
        )
    }
}



class SelectComparisonScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { selectComparisonRoute() }
}
