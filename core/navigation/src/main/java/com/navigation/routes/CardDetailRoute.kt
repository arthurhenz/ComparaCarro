package com.navigation.routes

import androidx.navigation3.runtime.NavKey
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class CardDetailRoute(val cardId: String) : NavKey

fun Navigator.navigateToDetail(cardId:String) {
    navigate(CardDetailRoute(cardId), NavOptions(singleTop = true))
} // REPLICA PARA OS OUTROS
