package com.data.usecase

import com.data.model.PaginationResult
import com.data.model.SmallCardData
import com.data.repository.CardRepository
import org.koin.core.annotation.Single

@Single
class GetSmallCardsPageUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int
    ): PaginationResult<SmallCardData> {
        return cardRepository.getSmallCardsPage(page, pageSize)
    }
}
