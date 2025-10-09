package com.data.model

import com.data.R

data class SmallCardData(
    val id: String,
    val title: String,
    val price: String,
    val selected: Boolean = false,
    val backgroundRes: Int = R.drawable.ic_launcher_background
)
