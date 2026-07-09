package com.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A favorited car as it is durably stored in Room. This is a self-contained snapshot of the
 * fields the Favorites screen renders, so the list can be shown instantly on a cold start
 * without any network call. Fields unknown at save-time (e.g. [range]) are stored blank.
 *
 * [id] is the "modelSlug,fuelAcronym,year" vehicle spec used everywhere else in the app.
 */
@Entity(tableName = "favorite_cars")
data class FavoriteCarEntity(
    @PrimaryKey val id: String,
    val brand: String,
    val title: String,
    val price: String,
    val powertrain: String,
    val range: String,
    val imageUrl: String?,
    val addedAt: Long,
)
