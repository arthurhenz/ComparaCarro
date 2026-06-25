package com.navigation.routes

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class CompareScreenRoute(
    val firstModelSlug: String,
    val firstFuelAcronym: String,
    val firstYear: String,
    val secondModelSlug: String,
    val secondFuelAcronym: String,
    val secondYear: String,
) : NavKey
