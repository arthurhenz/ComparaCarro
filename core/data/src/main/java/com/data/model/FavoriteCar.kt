package com.data.model

/**
 * A favorited car in domain terms. [id] is the "modelSlug,fuelAcronym,year" vehicle spec.
 * [addedAt] is set by the repository when the car is saved; callers can leave it at 0.
 */
data class FavoriteCar(
    val id: String,
    val brand: String,
    val title: String,
    val price: String,
    val powertrain: String = "",
    val range: String = "",
    val imageUrl: String? = null,
    val addedAt: Long = 0L,
)
