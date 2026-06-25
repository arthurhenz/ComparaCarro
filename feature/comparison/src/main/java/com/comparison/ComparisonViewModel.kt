package com.comparison

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.data.usecase.GetCarUseCase
import com.navigation.routes.FavoritesRoute
import com.navigation.routes.HomeScreenRoute
import com.navigation.routes.ProfileRoute
import com.navigation.routes.SelectComparisonRoute
import com.navigation.routes.parseVehicleSpec
import com.ui.BottomNavTab
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

data class ComparisonParams(val firstSpec: String, val secondSpec: String)

@KoinViewModel
class ComparisonViewModel(
    private val getCarUseCase: GetCarUseCase,
    private val params: ComparisonParams,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {
    private val _state = MutableStateFlow<ComparisonScreenState>(ComparisonScreenState.Loading)
    val state: StateFlow<ComparisonScreenState> = _state.asStateFlow()

    init {
        loadCardComparisons()
    }

    private fun loadCardComparisons() =
        viewModelScope.launch {
            try {
                Log.d("ComparisonViewModel", "Comparing '${params.firstSpec}' vs '${params.secondSpec}'")

                val (firstSlug, firstFuel, firstYear) = parseVehicleSpec(params.firstSpec)
                val (secondSlug, secondFuel, secondYear) = parseVehicleSpec(params.secondSpec)

                val firstDeferred = async { getCarUseCase(firstSlug, firstFuel, firstYear) }
                val secondDeferred = async { getCarUseCase(secondSlug, secondFuel, secondYear) }

                _state.value =
                    ComparisonScreenState.Success(
                        firstCar = firstDeferred.await(),
                        secondCar = secondDeferred.await(),
                    )
            } catch (e: Exception) {
                Log.e("ComparisonViewModel", "Error loading cars", e)
                _state.value = ComparisonScreenState.Error(e.message ?: "Failed to load car comparisons")
            }
        }

    fun navigateToHome() {
        navigate(HomeScreenRoute, NavOptions(popUpTo = HomeScreenRoute, popUpToInclusive = true))
    }

    fun navigateToBottomTab(tab: BottomNavTab) {
        when (tab) {
            BottomNavTab.Garagem -> navigate(HomeScreenRoute, NavOptions(popUpTo = HomeScreenRoute))
            BottomNavTab.Comparar -> navigate(SelectComparisonRoute(null), NavOptions(singleTop = true))
            BottomNavTab.Favoritos -> navigate(FavoritesRoute, NavOptions(singleTop = true))
            BottomNavTab.Perfil -> navigate(ProfileRoute, NavOptions(singleTop = true))
        }
    }

    fun onEvent(event: ComparisonScreenEvent) {
        when (event) {
            ComparisonScreenEvent.ReloadCard -> {
                loadCardComparisons()
            }
            is ComparisonScreenEvent.ToggleFavorite -> {
            }
            is ComparisonScreenEvent.LoadRelatedCards -> {
                loadCardComparisons()
            }
        }
    }
}
