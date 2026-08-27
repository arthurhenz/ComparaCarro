package com.data.usecase

import com.data.model.FavoriteCar
import com.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class ObserveAllFavoritesUseCase(
    private val favoriteRepository: FavoriteRepository,
) {
    operator fun invoke(): Flow<List<FavoriteCar>> = favoriteRepository.observeAll()
}
