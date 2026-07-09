package com.detail

sealed class DetailScreenEvent {
    data object ReloadCard : DetailScreenEvent()
    data object ToggleFavorite : DetailScreenEvent()
    data class LoadRelatedCards(val cardId: String) : DetailScreenEvent()
}
