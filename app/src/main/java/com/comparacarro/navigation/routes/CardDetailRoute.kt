package com.comparacarro.navigation.routes

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.detail.DetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class CardDetailRoute(val cardId: String) : NavKey

fun EntryProviderScope<NavKey>.cardDetailRoute(
    goBack: () -> Unit,
    onCompareFromDetail: (String) -> Unit
) {
    entry<CardDetailRoute> { key ->
        DetailScreen(
            cardId = key.cardId,
            onBackClick = goBack,
            onCompareClick = { firstId -> onCompareFromDetail(firstId) }
        )
    }
}
