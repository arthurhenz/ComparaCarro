package com.data.usecase

import com.data.repository.FavoriteRepository
import org.koin.core.annotation.Factory

@Factory
class RemoveFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository,
) {
    suspend operator fun invoke(id: String) = favoriteRepository.remove(id)
}
