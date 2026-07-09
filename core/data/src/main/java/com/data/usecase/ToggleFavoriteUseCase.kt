package com.data.usecase

import com.data.model.FavoriteCar
import com.data.repository.FavoriteRepository
import org.koin.core.annotation.Factory

@Factory
class ToggleFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository,
) {
    /** Adds or removes the car. Returns the new favorited state (true = now favorited). */
    suspend operator fun invoke(car: FavoriteCar): Boolean = favoriteRepository.toggle(car)
}
