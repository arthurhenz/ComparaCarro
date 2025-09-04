package com.data.usecase

import com.data.model.LargeCardData
import com.data.repository.CardRepository

class GetLargeCardsUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(): List<LargeCardData> {
        return cardRepository.getLargeCards()
    }
}
