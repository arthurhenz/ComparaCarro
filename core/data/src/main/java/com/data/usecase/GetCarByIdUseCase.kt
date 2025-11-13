package com.data.usecase

import com.data.model.CarDetailData
import com.data.repository.CardRepository
import org.koin.core.annotation.Single

@Single
class GetCarByIdUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(id: Int): CarDetailData {
        return cardRepository.getCarById(id)
    }
}



