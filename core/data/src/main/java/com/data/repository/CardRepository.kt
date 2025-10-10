package com.data.repository

import com.data.model.CarDetailData
import com.data.model.LargeCardData
import com.data.model.SmallCardData

interface CardRepository {
    suspend fun getLargeCards(): List<LargeCardData>
    suspend fun getSmallCards(): List<SmallCardData>
    suspend fun getCarById(id: Int): CarDetailData
}
