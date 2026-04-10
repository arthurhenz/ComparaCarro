package com.comparacarro.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey
import com.common.navigation.NavOptions
import com.common.navigation.Navigator

class AppNavigator : Navigator {
    val backStack = mutableStateListOf<NavKey>()

    fun setStartDestination(route: NavKey) {
        if (backStack.isEmpty()) {
            backStack.add(route)
        }
    }

    override fun navigate(
        route: Any,
        options: NavOptions
    ) {
        val navRoute = route as NavKey

        // Usar navOption aqui

        if (options.popUpTo != null) {
            val popTarget = options.popUpTo!!
            val targetIndex = backStack.indexOfLast { it::class == popTarget::class }
            if (targetIndex >= 0) {
                val removeFrom = if (options.popUpToInclusive) targetIndex else targetIndex + 1
                while (backStack.size > removeFrom) {
                    backStack.removeLast()
                }
            }
        }

        if (options.replaceTop && backStack.isNotEmpty()) {
            backStack.removeLast()
        }

        if (options.singleTop && backStack.lastOrNull()?.let { it::class == navRoute::class } == true) {
            return
        }

        backStack.add(navRoute)
    }

    override fun goBack() {
        if (backStack.size > 1) {
            backStack.removeLast()
        }
    }
}
