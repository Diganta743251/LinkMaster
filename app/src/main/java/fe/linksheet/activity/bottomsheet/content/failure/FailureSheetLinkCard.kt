package fe.linksheet.activity.bottomsheet.content.failure

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter

// Minimal local stubs to avoid external dependencies
private object AlertCardDefaults {
    val MinHeight: Modifier = Modifier
    val InnerPadding: PaddingValues = PaddingValues(12.dp)
    val HorizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp)
    val IconSize: Dp = 24.dp
    val IconContainerSize: Dp = 36.dp
}

private object CustomShapeDefaults {
    val SingleShape: Shape @Composable get() = MaterialTheme.shapes.medium
}

private class IconOffset
private class TextContent(val content: @Composable () -> Unit)
private typealias OptionalContent = (@Composable () -> Unit)?
private typealias IconPainter = ImageVector

@Composable
fun FailureSheetLinkCard(
    modifier: Modifier = AlertCardDefaults.MinHeight,
    colors: CardColors = CardDefaults.cardColors(),
    shape: Shape = CustomShapeDefaults.SingleShape,
    onClick: (() -> Unit)? = null,
    innerPadding: PaddingValues = AlertCardDefaults.InnerPadding,
    horizontalArrangement: Arrangement.Horizontal = AlertCardDefaults.HorizontalArrangement,
    text: @Composable () -> Unit,
    icon: ImageVector,
    iconContentDescription: String?,
    content: (@Composable () -> Unit)? = null,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        colors = colors
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier)
                .padding(innerPadding),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = rememberVectorPainter(image = icon),
                contentDescription = iconContentDescription,
                tint = contentColorFor(backgroundColor = colors.containerColor)
            )

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyMedium,
                content = text
            )
        }

        if (content != null) {
            Box(modifier = Modifier.padding(AlertCardDefaults.InnerPadding)) {
                content()
            }
        }
    }
}
