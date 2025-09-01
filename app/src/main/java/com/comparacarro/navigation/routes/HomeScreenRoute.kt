package com.comparacarro.navigation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.comparacarro.navigation.Screen
import com.comparacarro.screens.HomeScreen

fun NavGraphBuilder.homeScreenRoute(
    onCouponClick: (Int) -> Unit
) {
    composable(
        route = Screen.Home.route
    ) {
        HomeScreenRoute(
            onCouponClick = onCouponClick
        )
    }
}

@Composable
private fun HomeScreenRoute(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCouponClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        onCouponClick = onCouponClick,
        selectedCategory = getSelectedCategory(state),
        onCategorySelected = { category ->
            viewModel.onEvent(HomeScreenEvent.FilterByCategory(category))
        },
        state = state
    )
}

private fun getSelectedCategory(state: HomeScreenState): CouponCategory {
    return when (state) {
        is HomeScreenState.FilteredCoupons -> state.category
        else -> CouponCategory.TODOS
    }
}
