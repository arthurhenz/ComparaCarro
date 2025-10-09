package com.data.usecase

import com.data.repository.RecentlyViewedRepository
import org.koin.core.annotation.Factory

@Factory
class SaveRecentlyViewedCarUseCase(
    private val recentlyViewedRepository: RecentlyViewedRepository
) {
    suspend operator fun invoke(carId: String) {
        recentlyViewedRepository.addRecentlyViewedCarId(carId)
    }
}

