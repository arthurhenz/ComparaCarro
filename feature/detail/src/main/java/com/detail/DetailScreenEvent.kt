package com.detail

sealed class DetailScreenEvent {
    data object ReloadCard : DetailScreenEvent()
    data class ToggleFavorite(val cardId: String) : DetailScreenEvent()
    data class LoadRelatedCards(val cardId: String) : DetailScreenEvent()
}
