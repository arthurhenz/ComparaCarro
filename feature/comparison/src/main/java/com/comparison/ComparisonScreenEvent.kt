package com.comparison

sealed class ComparisonScreenEvent {
    data object ReloadCard : ComparisonScreenEvent()
    data class ToggleFavorite(val cardId: String) : ComparisonScreenEvent()
    data class LoadRelatedCards(val cardId: String) : ComparisonScreenEvent()
}
