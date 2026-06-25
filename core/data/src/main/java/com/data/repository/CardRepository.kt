package com.data.repository

import com.data.model.CarDetailData
import com.data.model.LargeCardData
import com.data.model.PaginationResult
import com.data.model.SmallCardData

interface CardRepository {
    suspend fun getLargeCards(): List<LargeCardData>
    suspend fun getSmallCards(): List<SmallCardData>
    suspend fun getSmallCardsPage(
        page: Int,
        pageSize: Int,
        query: String? = null,
    ): PaginationResult<SmallCardData>

    /** Loads a single vehicle's detail (expanded price) identified by model slug + fuel + year. */
    suspend fun getCar(
        modelSlug: String,
        fuelAcronym: String,
        year: String,
    ): CarDetailData
}
