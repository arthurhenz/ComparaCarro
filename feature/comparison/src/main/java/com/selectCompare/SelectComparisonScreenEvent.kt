package com.selectCompare

sealed class SelectComparisonScreenEvent {
    data object ReloadCard : SelectComparisonScreenEvent()
    data class ToggleFavorite(val cardId: String) : SelectComparisonScreenEvent()
    data class LoadRelatedCards(val cardId: String) : SelectComparisonScreenEvent()
}
