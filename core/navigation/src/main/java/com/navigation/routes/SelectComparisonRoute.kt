package com.navigation.routes

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class SelectComparisonRoute(val firstId: String?) : NavKey
