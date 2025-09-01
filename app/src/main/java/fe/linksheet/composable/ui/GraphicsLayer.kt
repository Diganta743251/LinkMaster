package fe.linksheet.composable.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.graphicsLayer

// Provide a helper that maps to existing graphicsLayer lambda receiver usage
fun Modifier.withGraphicsLayer(
    scaleX: Float? = null,
    scaleY: Float? = null,
    alpha: Float? = null,
    translationX: Float? = null,
    translationY: Float? = null,
    shadowElevation: Float? = null,
    renderEffect: RenderEffect? = null,
): Modifier = this.graphicsLayer {
    scaleX?.let { this.scaleX = it }
    scaleY?.let { this.scaleY = it }
    alpha?.let { this.alpha = it }
    translationX?.let { this.translationX = it }
    translationY?.let { this.translationY = it }
    shadowElevation?.let { this.shadowElevation = it }
    // Set renderEffect if provided (API dependant on compose version)
    renderEffect?.let { this.renderEffect = it }
}
