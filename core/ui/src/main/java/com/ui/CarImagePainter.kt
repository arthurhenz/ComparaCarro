package com.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter

/**
 * Loads a remote car image [imageUrl] with Coil, falling back to the shared placeholder
 * while loading, on error, or when the URL is null. Returns a [Painter] so existing
 * composables that take an image painter need no signature change.
 */
@Composable
fun rememberCarImagePainter(imageUrl: String?): Painter {
    val placeholder = painterResource(id = R.drawable.ic_launcher_background)
    return rememberAsyncImagePainter(
        model = imageUrl,
        placeholder = placeholder,
        error = placeholder,
        fallback = placeholder,
        contentScale = ContentScale.Crop,
    )
}
