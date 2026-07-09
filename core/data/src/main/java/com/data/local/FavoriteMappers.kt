package com.data.local

import com.data.model.FavoriteCar

fun FavoriteCarEntity.toDomain(): FavoriteCar =
    FavoriteCar(
        id = id,
        brand = brand,
        title = title,
        price = price,
        powertrain = powertrain,
        range = range,
        imageUrl = imageUrl,
        addedAt = addedAt,
    )

fun FavoriteCar.toEntity(addedAt: Long): FavoriteCarEntity =
    FavoriteCarEntity(
        id = id,
        brand = brand,
        title = title,
        price = price,
        powertrain = powertrain,
        range = range,
        imageUrl = imageUrl,
        addedAt = addedAt,
    )
