package fe.linksheet.composable.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells as ComposeGridCells

object GridCells {
    fun Fixed(count: Int) = ComposeGridCells.Fixed(count)
    fun Adaptive(minSize: androidx.compose.ui.unit.Dp) = ComposeGridCells.Adaptive(minSize)
}
