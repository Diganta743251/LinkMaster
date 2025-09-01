package fe.linksheet.composable.ui

import androidx.compose.material3.TabPosition
import androidx.compose.ui.unit.Dp

fun TabPosition.tabIndicatorOffset(): Dp {
    return (left + right) / 2f
}
