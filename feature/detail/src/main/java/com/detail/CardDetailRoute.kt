package com.detail

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.navigation.EntryProvider
import com.navigation.routes.CardDetailRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun EntryProviderScope<NavKey>.cardDetailRoute() {
    entry<CardDetailRoute> { key ->
        val viewModel: DetailViewModel = koinViewModel { parametersOf(key.modelSlug, key.fuelAcronym, key.year) }

        val state by viewModel.state.collectAsStateWithLifecycle()

        DetailScreen(
            state = state,
            onBackClick = viewModel::goBack,
            onCompareClick = viewModel::navigateToCompare,
            onToggleFavorite = { viewModel.onEvent(DetailScreenEvent.ToggleFavorite) },
        )
    }
}

class CardDetailScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { cardDetailRoute() }
}
