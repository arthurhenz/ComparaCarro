package com.data.model

data class SmallCardData(
    val id: String,
    val title: String,
    val price: String,
    val selected: Boolean = false,
    val backgroundRes: Int = com.ui.R.drawable.ic_launcher_background
)
