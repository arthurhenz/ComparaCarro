package com.detail

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.navigation.EntryProvider
import com.navigation.routes.CardDetailRoute
import org.koin.compose.module.rememberKoinModules
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.ksp.generated.module

fun EntryProviderScope<NavKey>.cardDetailRoute() {
    entry<CardDetailRoute> { key ->
        rememberKoinModules(unloadOnForgotten = true, unloadOnAbandoned = true) {
            listOf(DetailModule().module)
        }

        val viewModel: DetailViewModel = koinViewModel { parametersOf(key.cardId) }

        val state by viewModel.state.collectAsState()

        DetailScreen(
            state = state,
            onBackClick = viewModel::goBack,
            onCompareClick = viewModel::navigateToCompare
        )
    }
}

class CardDetailScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { cardDetailRoute() }
}
