package com.navigation.routes

import androidx.navigation3.runtime.NavKey
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class CardDetailRoute(
    val modelSlug: String,
    val fuelAcronym: String,
    val year: String,
) : NavKey

/** Navigates to the detail screen from a `model-slug,fuel-acronym,year` spec. */
fun Navigator.navigateToDetail(spec: String) {
    val (modelSlug, fuelAcronym, year) = parseVehicleSpec(spec)
    navigate(CardDetailRoute(modelSlug, fuelAcronym, year), NavOptions(singleTop = true))
}
