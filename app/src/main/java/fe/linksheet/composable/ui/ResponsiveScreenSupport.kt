package fe.linksheet.composable.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Enhanced Responsive Screen Size Support for 2025
 * Supports phones, tablets, foldables, and desktop layouts
 */

enum class ScreenSize {
    COMPACT,    // < 600dp (phones)
    MEDIUM,     // 600-840dp (tablets, unfolded phones)
    EXPANDED    // > 840dp (large tablets, desktop)
}

enum class ScreenOrientation {
    PORTRAIT,
    LANDSCAPE
}

data class ScreenInfo(
    val size: ScreenSize,
    val orientation: ScreenOrientation,
    val widthDp: Int,
    val heightDp: Int,
    val isTablet: Boolean,
    val isFoldable: Boolean,
    val isDesktop: Boolean
)

/**
 * Get current screen information
 */
@Composable
fun rememberScreenInfo(): ScreenInfo {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    
    return remember(configuration.screenWidthDp, configuration.screenHeightDp, configuration.orientation) {
        val widthDp = configuration.screenWidthDp
        val heightDp = configuration.screenHeightDp
        
        val size = when {
            widthDp < 600 -> ScreenSize.COMPACT
            widthDp < 840 -> ScreenSize.MEDIUM
            else -> ScreenSize.EXPANDED
        }
        
        val orientation = if (widthDp > heightDp) {
            ScreenOrientation.LANDSCAPE
        } else {
            ScreenOrientation.PORTRAIT
        }
        
        val isTablet = widthDp >= 600
        val isFoldable = (widthDp > 700 && heightDp < 500) || (heightDp > 700 && widthDp < 500)
        val isDesktop = widthDp >= 840
        
        ScreenInfo(
            size = size,
            orientation = orientation,
            widthDp = widthDp,
            heightDp = heightDp,
            isTablet = isTablet,
            isFoldable = isFoldable,
            isDesktop = isDesktop
        )
    }
}

/**
 * Responsive padding based on screen size
 */
@Composable
fun responsivePadding(): PaddingValues {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> PaddingValues(16.dp)
        ScreenSize.MEDIUM -> PaddingValues(24.dp)
        ScreenSize.EXPANDED -> PaddingValues(32.dp)
    }
}

/**
 * Responsive horizontal padding
 */
@Composable
fun responsiveHorizontalPadding(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> 16.dp
        ScreenSize.MEDIUM -> 32.dp
        ScreenSize.EXPANDED -> 48.dp
    }
}

/**
 * Responsive vertical padding
 */
@Composable
fun responsiveVerticalPadding(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> 12.dp
        ScreenSize.MEDIUM -> 16.dp
        ScreenSize.EXPANDED -> 20.dp
    }
}

/**
 * Responsive card elevation
 */
@Composable
fun responsiveCardElevation(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> 4.dp
        ScreenSize.MEDIUM -> 6.dp
        ScreenSize.EXPANDED -> 8.dp
    }
}

/**
 * Responsive grid columns
 */
@Composable
fun responsiveGridColumns(): Int {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> if (screenInfo.orientation == ScreenOrientation.LANDSCAPE) 3 else 2
        ScreenSize.MEDIUM -> if (screenInfo.orientation == ScreenOrientation.LANDSCAPE) 4 else 3
        ScreenSize.EXPANDED -> if (screenInfo.orientation == ScreenOrientation.LANDSCAPE) 6 else 4
    }
}

/**
 * Responsive font scale
 */
@Composable
fun responsiveFontScale(): Float {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> 1.0f
        ScreenSize.MEDIUM -> 1.1f
        ScreenSize.EXPANDED -> 1.2f
    }
}

/**
 * Enhanced Adaptive Layout with foldable support
 */
@Composable
fun EnhancedAdaptiveLayout(
    compactContent: @Composable () -> Unit,
    mediumContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
    foldableContent: (@Composable () -> Unit)? = null
) {
    val screenInfo = rememberScreenInfo()
    
    when {
        screenInfo.isFoldable && foldableContent != null -> foldableContent()
        screenInfo.size == ScreenSize.COMPACT -> compactContent()
        screenInfo.size == ScreenSize.MEDIUM -> mediumContent()
        screenInfo.size == ScreenSize.EXPANDED -> expandedContent()
        else -> compactContent()
    }
}

/**
 * Responsive Navigation Layout
 */
@Composable
fun ResponsiveNavigationLayout(
    navigationContent: @Composable () -> Unit,
    mainContent: @Composable () -> Unit
) {
    val screenInfo = rememberScreenInfo()
    
    when (screenInfo.size) {
        ScreenSize.COMPACT -> {
            // Bottom navigation for phones
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    mainContent()
                }
                navigationContent()
            }
        }
        ScreenSize.MEDIUM -> {
            if (screenInfo.orientation == ScreenOrientation.LANDSCAPE) {
                // Side navigation for landscape tablets
                Row(modifier = Modifier.fillMaxSize()) {
                    navigationContent()
                    Box(modifier = Modifier.weight(1f)) {
                        mainContent()
                    }
                }
            } else {
                // Bottom navigation for portrait tablets
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f)) {
                        mainContent()
                    }
                    navigationContent()
                }
            }
        }
        ScreenSize.EXPANDED -> {
            // Side navigation for desktop/large tablets
            Row(modifier = Modifier.fillMaxSize()) {
                navigationContent()
                Box(modifier = Modifier.weight(1f)) {
                    mainContent()
                }
            }
        }
    }
}

/**
 * Responsive Dialog Width
 */
@Composable
fun responsiveDialogWidth(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> (screenInfo.widthDp * 0.9f).dp
        ScreenSize.MEDIUM -> (screenInfo.widthDp * 0.7f).dp.coerceAtMost(600.dp)
        ScreenSize.EXPANDED -> (screenInfo.widthDp * 0.5f).dp.coerceAtMost(800.dp)
    }
}

/**
 * Responsive Bottom Sheet Height
 */
@Composable
fun responsiveBottomSheetHeight(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> (screenInfo.heightDp * 0.8f).dp
        ScreenSize.MEDIUM -> (screenInfo.heightDp * 0.7f).dp
        ScreenSize.EXPANDED -> (screenInfo.heightDp * 0.6f).dp
    }
}

/**
 * Responsive Content Width with max constraints
 */
@Composable
fun responsiveContentWidth(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> screenInfo.widthDp.dp
        ScreenSize.MEDIUM -> (screenInfo.widthDp * 0.9f).dp.coerceAtMost(700.dp)
        ScreenSize.EXPANDED -> (screenInfo.widthDp * 0.8f).dp.coerceAtMost(1200.dp)
    }
}

/**
 * Responsive List Item Height
 */
@Composable
fun responsiveListItemHeight(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> 72.dp
        ScreenSize.MEDIUM -> 80.dp
        ScreenSize.EXPANDED -> 88.dp
    }
}

/**
 * Responsive Icon Size
 */
@Composable
fun responsiveIconSize(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> 24.dp
        ScreenSize.MEDIUM -> 28.dp
        ScreenSize.EXPANDED -> 32.dp
    }
}

/**
 * Responsive Button Height
 */
@Composable
fun responsiveButtonHeight(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> 48.dp
        ScreenSize.MEDIUM -> 52.dp
        ScreenSize.EXPANDED -> 56.dp
    }
}

/**
 * Responsive FAB Size
 */
@Composable
fun responsiveFabSize(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> 56.dp
        ScreenSize.MEDIUM -> 64.dp
        ScreenSize.EXPANDED -> 72.dp
    }
}

/**
 * Responsive Card Corner Radius
 */
@Composable
fun responsiveCardCornerRadius(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> 12.dp
        ScreenSize.MEDIUM -> 16.dp
        ScreenSize.EXPANDED -> 20.dp
    }
}

/**
 * Responsive Spacing
 */
@Composable
fun responsiveSpacing(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> 8.dp
        ScreenSize.MEDIUM -> 12.dp
        ScreenSize.EXPANDED -> 16.dp
    }
}

/**
 * Responsive Large Spacing
 */
@Composable
fun responsiveLargeSpacing(): Dp {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> 16.dp
        ScreenSize.MEDIUM -> 24.dp
        ScreenSize.EXPANDED -> 32.dp
    }
}

/**
 * Responsive Typography Scale
 */
@Composable
fun responsiveTypographyScale(): Float {
    val screenInfo = rememberScreenInfo()
    
    return when (screenInfo.size) {
        ScreenSize.COMPACT -> 1.0f
        ScreenSize.MEDIUM -> 1.05f
        ScreenSize.EXPANDED -> 1.1f
    }
}