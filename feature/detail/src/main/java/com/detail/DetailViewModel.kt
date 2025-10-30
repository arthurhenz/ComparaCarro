package com.detail

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
class DetailViewModel(
    private val getCarByIdUseCase: GetCarByIdUseCase,
    @InjectedParam private val cardId: String
) : ViewModel() {
    private val _state = MutableStateFlow<DetailScreenState>(DetailScreenState.Loading)
    val state: StateFlow<DetailScreenState> = _state.asStateFlow()

    init {
        loadCardDetails()
    }

    private fun loadCardDetails() =
        viewModelScope.launch {
            try {
                Log.d("DetailViewModel", "Received cardId='$cardId'")
                val id =
                    cardId.toIntOrNull()
                        ?: throw IllegalArgumentException("Invalid id: $cardId")
                val car = getCarByIdUseCase(id)
                _state.value = DetailScreenState.Success(car = car)
            } catch (e: Exception) {
                _state.value = DetailScreenState.Error(e.message ?: "Failed to load card details")
            }
        }

    fun onEvent(event: DetailScreenEvent) {
        when (event) {
            DetailScreenEvent.ReloadCard -> {
                loadCardDetails()
            }
            is DetailScreenEvent.ToggleFavorite -> {
            }
            is DetailScreenEvent.LoadRelatedCards -> {
                loadCardDetails()
            }
        }
    }
}
