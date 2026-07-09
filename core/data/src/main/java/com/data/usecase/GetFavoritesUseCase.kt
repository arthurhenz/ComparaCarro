package com.data.usecase

import androidx.paging.PagingData
import com.data.model.FavoriteCar
import com.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetFavoritesUseCase(
    private val favoriteRepository: FavoriteRepository,
) {
    operator fun invoke(): Flow<PagingData<FavoriteCar>> = favoriteRepository.favorites
}
