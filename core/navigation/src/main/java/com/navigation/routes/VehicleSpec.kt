package com.navigation.routes

/**
 * Throughout the app a vehicle is identified by a `model-slug,fuel-acronym,year` spec
 * (the same format consumed by the fipeX `/v1/prices/compare` endpoint). This parses it
 * into its three parts.
 */
fun parseVehicleSpec(spec: String): Triple<String, String, String> {
    val parts = spec.split(",")
    return Triple(parts.getOrElse(0) { "" }, parts.getOrElse(1) { "" }, parts.getOrElse(2) { "" })
}
