package com.data.repository

import com.data.model.LargeCardData
import com.data.model.SmallCardData

class CardRepositoryImpl : CardRepository {
    
    override suspend fun getLargeCards(): List<LargeCardData> {
        return listOf(
            LargeCardData(
                id = "large_card_1",
                title = "Novidades do mês"
            ),
            LargeCardData(
                id = "large_card_2",
                title = "Ofertas imperdíveis"
            )
        )
    }
    
    override suspend fun getSmallCards(): List<SmallCardData> {
        return listOf(
            SmallCardData(
                id = "small_card_1",
                title = "Volkswagen Saveiro 2017",
                price = "R$ 55.900",
                selected = true
            ),
            SmallCardData(
                id = "small_card_2",
                title = "Audi A4 Quattro Sedan 2019",
                price = "R$ 142.000",
                selected = false
            ),
            SmallCardData(
                id = "small_card_3",
                title = "Honda Civic Si LX LXS 2020",
                price = "R$ 115.500",
                selected = false
            ),
            SmallCardData(
                id = "small_card_4",
                title = "Toyota Corolla Xei Guerra Corolla Siria 2021",
                price = "R$ 128.000",
                selected = true
            )
        )
    }
}
