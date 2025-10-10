package com.selectCompare


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.usecase.GetCarByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class SelectComparisonViewModel(
    private val getCarByIdUseCase: GetCarByIdUseCase,
    @InjectedParam private val cardId: String
) : ViewModel() {

    private val _state = MutableStateFlow<SelectComparisonScreenState>(SelectComparisonScreenState.Loading)
    val state: StateFlow<SelectComparisonScreenState> = _state.asStateFlow()

    init {
        loadCardComparisons()
    }

    private fun loadCardComparisons() = viewModelScope.launch {
        try {
            Log.d("ComparisonViewModel", "Received cardId='$cardId'")
            val id = cardId.toIntOrNull()
                ?: throw IllegalArgumentException("Invalid id: $cardId")
            val car = getCarByIdUseCase(id)
            _state.value = SelectComparisonScreenState.Success(car = car)
        } catch (e: Exception) {
            _state.value = SelectComparisonScreenState.Error(e.message ?: "Failed to load card Comparisons")
        }
    }

    fun onEvent(event: SelectComparisonScreenEvent) {
        when (event) {
            SelectComparisonScreenEvent.ReloadCard -> {
                loadCardComparisons()
            }
            is SelectComparisonScreenEvent.ToggleFavorite -> {
                // Handle favorite toggle logic here
                // This would typically call a use case to update favorite status
            }
            is SelectComparisonScreenEvent.LoadRelatedCards -> {
                // Handle loading related cards
                loadCardComparisons()
            }
        }
    }
}
