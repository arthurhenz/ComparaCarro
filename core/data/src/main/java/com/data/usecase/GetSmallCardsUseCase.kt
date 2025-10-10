package com.data.usecase

import com.data.model.SmallCardData
import com.data.repository.CardRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Single

@Single
class GetSmallCardsUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(): List<SmallCardData> {
        return cardRepository.getSmallCards()
    }
}
