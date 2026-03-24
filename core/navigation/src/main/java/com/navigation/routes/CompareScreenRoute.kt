package com.navigation.routes

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class CompareScreenRoute(val firstId: String, val secondId: String) : NavKey
