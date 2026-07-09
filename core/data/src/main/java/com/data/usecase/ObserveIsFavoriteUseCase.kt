package com.data.usecase

import com.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class ObserveIsFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository,
) {
    operator fun invoke(id: String): Flow<Boolean> = favoriteRepository.observeIsFavorite(id)
}
