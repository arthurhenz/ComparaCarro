package com.data.usecase

import com.data.model.LargeCardData
import com.data.repository.CardRepository
import com.data.repository.RecentlyViewedRepository
import org.koin.core.annotation.Factory

@Factory
class GetRecentlyViewedCarsUseCase(
    private val recentlyViewedRepository: RecentlyViewedRepository,
    private val cardRepository: CardRepository,
) {
    suspend operator fun invoke(): List<LargeCardData> {
        val recentlyViewedIds = recentlyViewedRepository.getRecentlyViewedCarIdsSync()

        return recentlyViewedIds.mapNotNull { carId ->
            try {
                // carId is a "model-slug,fuel-acronym,year" spec.
                val parts = carId.split(",")
                if (parts.size < 3) return@mapNotNull null
                val carDetail = cardRepository.getCar(parts[0], parts[1], parts[2])
                LargeCardData(
                    id = carDetail.id,
                    title = carDetail.title,
                )
            } catch (e: Exception) {
                android.util.Log.e("GetRecentlyViewedCarsUseCase", "Failed to load car $carId: ${e.message}")
                null
            }
        }
    }
}
