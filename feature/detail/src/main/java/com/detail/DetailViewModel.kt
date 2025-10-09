package com.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.usecase.GetSmallCardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

@KoinViewModel
class DetailViewModel(
    private val getSmallCardsUseCase: GetSmallCardsUseCase,
    private val cardId: String
) : ViewModel() {


    private val _state = MutableStateFlow<DetailScreenState>(DetailScreenState.Loading)
    val state: StateFlow<DetailScreenState> = _state.asStateFlow()

    init {
        loadCardDetails()
    }

    private fun loadCardDetails() = viewModelScope.launch {
        try {
            // For demo purposes, we'll simulate loading card details
            // In a real app, you'd have a use case to get specific card details
            val relatedCards = getSmallCardsUseCase()

            _state.value = DetailScreenState.Success(
                cardId = cardId,
                cardTitle = "Card Details for $cardId",
                cardDescription = "This is a detailed view of the selected card. Here you can see all the specifications, images, and additional information about this car.",
                relatedCards = relatedCards.take(4) // Show only first 4 related cards
            )
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
                // Handle favorite toggle logic here
                // This would typically call a use case to update favorite status
            }
            is DetailScreenEvent.LoadRelatedCards -> {
                // Handle loading related cards
                loadCardDetails()
            }
        }
    }
}
