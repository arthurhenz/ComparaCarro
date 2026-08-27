package com.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.common.utils.shimmerEffect
import com.theme.Theme

/**
 * Loads a remote car image [imageUrl] with Coil, falling back to the shared placeholder on
 * error or when the URL is null. While loading the painter draws nothing, so the slot can keep
 * shimmering via [shimmerWhileLoading] and the image appears with no placeholder frames in
 * between. Returns a [Painter] so existing composables that take an image painter need no
 * signature change.
 */
@Composable
fun rememberCarImagePainter(imageUrl: String?): Painter {
    val placeholder = ColorPainter(Theme.colors.surfaceRaised)
    return rememberAsyncImagePainter(
        model = imageUrl,
        error = placeholder,
        fallback = placeholder,
        contentScale = ContentScale.Crop,
    )
}

/**
 * Shimmers this element while [painter] is still resolving its image, handing off directly from
 * shimmer to the loaded bitmap. No-op for painters that are not Coil-backed (previews, local
 * resources) and once loading succeeds or fails.
 */
@Composable
fun Modifier.shimmerWhileLoading(painter: Painter): Modifier {
    val state = (painter as? AsyncImagePainter)?.state
    return if (state is AsyncImagePainter.State.Empty || state is AsyncImagePainter.State.Loading) {
        shimmerEffect()
    } else {
        this
    }
}
