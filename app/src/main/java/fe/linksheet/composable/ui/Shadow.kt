package fe.linksheet.composable.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp

fun Modifier.shadow(
    elevation: Dp,
    shape: androidx.compose.ui.graphics.Shape? = null,
    clip: Boolean = false,
    ambientColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
    spotColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified
): Modifier = this.shadow(
    elevation = elevation,
    shape = shape,
    clip = clip,
    ambientColor = ambientColor,
    spotColor = spotColor
)
