package com.favorites

sealed class FavoriteScreenEvent {
    data class RemoveFavorite(val id: String) : FavoriteScreenEvent()
}
