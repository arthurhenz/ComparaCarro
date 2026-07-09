package com.data.usecase

import com.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class ObserveFavoriteIdsUseCase(
    private val favoriteRepository: FavoriteRepository,
) {
    operator fun invoke(): Flow<Set<String>> = favoriteRepository.observeFavoriteIds()
}
