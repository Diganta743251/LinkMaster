package fe.linksheet.composable.ui

import androidx.compose.ui.graphics.drawscope.DrawScope

var cachedDrawResult: Any? = null

fun DrawScope.onDrawCacheReads(
    onDraw: (DrawScope) -> Unit
) {
    onDraw(this)
}
