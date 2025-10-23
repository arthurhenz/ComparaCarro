package com.comparison

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.usecase.GetCarByIdUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

data class ComparisonParams(val firstId: String, val secondId: String)

@KoinViewModel
class ComparisonViewModel(
    private val getCarByIdUseCase: GetCarByIdUseCase,
    private val params: ComparisonParams
) : ViewModel() {
    private val _state = MutableStateFlow<ComparisonScreenState>(ComparisonScreenState.Loading)
    val state: StateFlow<ComparisonScreenState> = _state.asStateFlow()

    init {
        loadCardComparisons()
    }

    private fun loadCardComparisons() =
        viewModelScope.launch {
            try {
                Log.d("ComparisonViewModel", "Received firstId='${params.firstId}', secondId='${params.secondId}'")

                val firstIdInt =
                    params.firstId.toIntOrNull()
                        ?: throw IllegalArgumentException("Invalid firstId: ${params.firstId}")
                val secondIdInt =
                    params.secondId.toIntOrNull()
                        ?: throw IllegalArgumentException("Invalid secondId: ${params.secondId}")

                val firstCarDeferred = async { getCarByIdUseCase(firstIdInt) }
                val secondCarDeferred = async { getCarByIdUseCase(secondIdInt) }

                val firstCar = firstCarDeferred.await()
                val secondCar = secondCarDeferred.await()

                _state.value =
                    ComparisonScreenState.Success(
                        firstCar = firstCar,
                        secondCar = secondCar
                    )
            } catch (e: Exception) {
                Log.e("ComparisonViewModel", "Error loading cars", e)
                _state.value = ComparisonScreenState.Error(e.message ?: "Failed to load car comparisons")
            }
        }

    fun onEvent(event: ComparisonScreenEvent) {
        when (event) {
            ComparisonScreenEvent.ReloadCard -> {
                loadCardComparisons()
            }
            is ComparisonScreenEvent.ToggleFavorite -> {
                // Handle favorite toggle logic here
                // This would typically call a use case to update favorite status
            }
            is ComparisonScreenEvent.LoadRelatedCards -> {
                // Handle loading related cards
                loadCardComparisons()
            }
        }
    }
}
