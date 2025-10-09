package com.data.repository

import com.data.model.LargeCardData
import com.data.model.SmallCardData
import jakarta.inject.Singleton
import org.koin.core.annotation.Single

interface CardRepository {
    suspend fun getLargeCards(): List<LargeCardData>
    suspend fun getSmallCards(): List<SmallCardData>
}
