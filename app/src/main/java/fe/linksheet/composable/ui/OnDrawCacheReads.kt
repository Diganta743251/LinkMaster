package fe.linksheet.composable.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.ContentDrawScope

// Correct signature for drawWithCache: returns onBuildDrawCache lambda and uses onDrawWithContent
fun Modifier.onDrawCacheReads(
    onBuild: DrawScope.() -> Unit
): Modifier = this.drawWithCache {
    // Build cache phase
    onBuild()
    onDrawWithContent {
        drawContent()
    }
}
