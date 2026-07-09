package com.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.data.model.CarDetailData
import com.data.model.FavoriteCar
import com.data.usecase.GetCarUseCase
import com.data.usecase.ObserveIsFavoriteUseCase
import com.data.usecase.ToggleFavoriteUseCase
import com.navigation.routes.SelectComparisonRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class DetailViewModel(
    private val getCarUseCase: GetCarUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val observeIsFavoriteUseCase: ObserveIsFavoriteUseCase,
    @InjectedParam private val modelSlug: String,
    @InjectedParam private val fuelAcronym: String,
    @InjectedParam private val year: String,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {
    private val _state = MutableStateFlow<DetailScreenState>(DetailScreenState.Loading)
    val state: StateFlow<DetailScreenState> = _state.asStateFlow()

    // Mirrors the persisted favorite state for the loaded car into the screen state.
    private var favoriteJob: Job? = null

    init {
        loadCardDetails()
    }

    private fun loadCardDetails() =
        viewModelScope.launch {
            try {
                Log.d("DetailViewModel", "Loading detail for $modelSlug,$fuelAcronym,$year")
                val car = getCarUseCase(modelSlug, fuelAcronym, year)
                _state.value = DetailScreenState.Success(car = car)
                observeFavorite(car.id)
            } catch (e: Exception) {
                _state.value = DetailScreenState.Error(e.message ?: "Failed to load card details")
            }
        }

    private fun observeFavorite(id: String) {
        favoriteJob?.cancel()
        favoriteJob =
            viewModelScope.launch {
                observeIsFavoriteUseCase(id).collect { isFavorite ->
                    val current = _state.value
                    if (current is DetailScreenState.Success && current.car.id == id) {
                        _state.value = current.copy(isFavorite = isFavorite)
                    }
                }
            }
    }

    private fun toggleFavorite() =
        viewModelScope.launch {
            val current = _state.value
            if (current is DetailScreenState.Success) {
                try {
                    toggleFavoriteUseCase(current.car.toFavoriteCar())
                } catch (e: Exception) {
                    Log.e("DetailViewModel", "Failed to toggle favorite: ${e.message}", e)
                }
            }
        }

    fun navigateToCompare(cardId: String) {
        navigate(SelectComparisonRoute(cardId), NavOptions(singleTop = true))
    }

    fun onEvent(event: DetailScreenEvent) {
        when (event) {
            DetailScreenEvent.ReloadCard -> {
                loadCardDetails()
            }
            DetailScreenEvent.ToggleFavorite -> {
                toggleFavorite()
            }
            is DetailScreenEvent.LoadRelatedCards -> {
                loadCardDetails()
            }
        }
    }
}

private fun CarDetailData.toFavoriteCar(): FavoriteCar =
    FavoriteCar(
        id = id,
        brand = makeName,
        title = title,
        price = price,
        powertrain = fuelName,
        range = "",
        imageUrl = imageUrl,
    )
