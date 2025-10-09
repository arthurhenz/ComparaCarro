package com.data.repository

import com.data.model.LargeCardData
import com.data.model.SmallCardData
import com.data.model.CarDetailData

interface CardRepository {
    suspend fun getLargeCards(): List<LargeCardData>
    suspend fun getSmallCards(): List<SmallCardData>
    suspend fun getCarById(id: Int): CarDetailData
}
