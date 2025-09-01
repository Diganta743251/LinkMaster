package fe.linksheet.composable.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

/**
 * Smart Adaptive Layouts for 2025 - Responsive design that adapts to screen size and orientation
 */

/**
 * Adaptive Layout that changes based on screen size
 */
@Composable
fun AdaptiveLayout(
    modifier: Modifier = Modifier,
    compactContent: @Composable () -> Unit,
    mediumContent: @Composable () -> Unit = compactContent,
    expandedContent: @Composable () -> Unit = mediumContent
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    when {
        screenWidth < 600.dp -> compactContent()
        screenWidth < 840.dp -> mediumContent()
        else -> expandedContent()
    }
}

/**
 * Smart Grid that adapts column count based on screen size
 */
@Composable
fun SmartAdaptiveGrid(
    items: List<Any>,
    modifier: Modifier = Modifier,
    minItemWidth: Int = 160,
    itemContent: @Composable LazyStaggeredGridItemScope.(item: Any, index: Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val columns = max(1, screenWidth / minItemWidth)
    
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(columns),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp
    ) {
        itemsIndexed(items) { index, item ->
            AnimatedVisibility(
                visible = true,
                enter = ModernAnimations.ScaleInWithBounce + fadeIn(
                    animationSpec = tween(300, delayMillis = index * 50)
                ),
                exit = ModernAnimations.ScaleOutWithFade
            ) {
                itemContent(item, index)
            }
        }
    }
}

/**
 * Masonry Layout for Pinterest-style grids
 */
@Composable
fun MasonryLayout(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val columnWidth = constraints.maxWidth / columns
        val columnHeights = IntArray(columns) { 0 }
        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = 0,
                    maxWidth = columnWidth
                )
            )
            
            // Find the shortest column
            val shortestColumnIndex = columnHeights.indices.minByOrNull { columnHeights[it] } ?: 0
            val x = shortestColumnIndex * columnWidth
            val y = columnHeights[shortestColumnIndex]
            
            columnHeights[shortestColumnIndex] += placeable.height
            
            placeable to (x to y)
        }
        
        val totalHeight = columnHeights.maxOrNull() ?: 0
        
        layout(constraints.maxWidth, totalHeight) {
            placeables.forEach { (placeable, position) ->
                placeable.place(position.first, position.second)
            }
        }
    }
}

/**
 * Flowing Layout that wraps items naturally
 */
@Composable
fun FlowLayout(
    modifier: Modifier = Modifier,
    horizontalSpacing: Int = 8,
    verticalSpacing: Int = 8,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0)) }
        
        var currentRow = 0
        var currentRowWidth = 0
        var currentRowHeight = 0
        val rowHeights = mutableListOf<Int>()
        val itemPositions = mutableListOf<Pair<Int, Int>>()
        
        placeables.forEach { placeable ->
            if (currentRowWidth + placeable.width > constraints.maxWidth) {
                // Start new row
                rowHeights.add(currentRowHeight)
                currentRow++
                currentRowWidth = 0
                currentRowHeight = 0
            }
            
            itemPositions.add(currentRowWidth to currentRow)
            currentRowWidth += placeable.width + horizontalSpacing
            currentRowHeight = max(currentRowHeight, placeable.height)
        }
        
        if (currentRowHeight > 0) {
            rowHeights.add(currentRowHeight)
        }
        
        val totalHeight = rowHeights.sumOf { it } + (rowHeights.size - 1) * verticalSpacing
        
        layout(constraints.maxWidth, totalHeight) {
            placeables.forEachIndexed { index, placeable ->
                val (x, row) = itemPositions[index]
                val y = rowHeights.take(row).sum() + row * verticalSpacing
                placeable.place(x, y)
            }
        }
    }
}

/**
 * Smart Card Layout with dynamic sizing
 */
@Composable
fun SmartCardLayout(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val isCompact = configuration.screenWidthDp < 600
    
    Modern3DCard(
        modifier = modifier.fillMaxWidth(),
        elevation = if (isCompact) 4.dp else 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isCompact) 16.dp else 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Box(
                        modifier = Modifier
                            .size(if (isCompact) 32.dp else 40.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            modifier = Modifier.size(if (isCompact) 16.dp else 20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = if (isCompact) {
                            MaterialTheme.typography.titleMedium
                        } else {
                            MaterialTheme.typography.titleLarge
                        }.copy(fontWeight = FontWeight.SemiBold)
                    )
                    
                    subtitle?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
                
                Row(content = actions)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Content
            content()
        }
    }
}

/**
 * Responsive Navigation Layout
 */
@Composable
fun ResponsiveNavigationLayout(
    navigationItems: List<ModernNavItem>,
    selectedItemId: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val useBottomNav = configuration.screenWidthDp < 840
    
    if (useBottomNav) {
        // Mobile layout with bottom navigation
        Column(modifier = modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                content()
            }
            
            ModernBottomNavigation(
                items = navigationItems,
                selectedItemId = selectedItemId,
                onItemSelected = onItemSelected
            )
        }
    } else {
        // Tablet/Desktop layout with navigation rail
        Row(modifier = modifier.fillMaxSize()) {
            ModernNavigationRail(
                items = navigationItems,
                selectedItemId = selectedItemId,
                onItemSelected = onItemSelected
            )
            
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                content()
            }
        }
    }
}

/**
 * Smart List Layout with dynamic item sizing
 */
@Composable
fun SmartListLayout(
    items: List<Any>,
    modifier: Modifier = Modifier,
    isCompact: Boolean = LocalConfiguration.current.screenWidthDp < 600,
    itemContent: @Composable LazyItemScope.(item: Any, index: Int, isCompact: Boolean) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = if (isCompact) 16.dp else 24.dp,
            vertical = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 12.dp)
    ) {
        itemsIndexed(items) { index, item ->
            AnimatedVisibility(
                visible = true,
                enter = ModernAnimations.SlideInFromRight + fadeIn(
                    animationSpec = tween(300, delayMillis = index * 30)
                ),
                exit = ModernAnimations.SlideOutToRight
            ) {
                itemContent(item, index, isCompact)
            }
        }
    }
}

/**
 * Adaptive Dialog that changes size based on screen
 */
@Composable
fun AdaptiveDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val isCompact = configuration.screenWidthDp < 600
    
    if (isCompact) {
        // Full-screen dialog on mobile
        ModernBottomSheet(
            onDismiss = onDismissRequest,
            modifier = modifier
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                
                Row(content = actions)
            }
            
            content()
        }
    } else {
        // Standard dialog on larger screens
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Column(content = content)
            },
            confirmButton = {
                Row(content = actions)
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = modifier.shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(20.dp)
            )
        )
    }
}

/**
 * Smart Spacing utility for responsive padding/margins
 */
object SmartSpacing {
    @Composable
    fun small() = if (LocalConfiguration.current.screenWidthDp < 600) 8.dp else 12.dp
    
    @Composable
    fun medium() = if (LocalConfiguration.current.screenWidthDp < 600) 16.dp else 24.dp
    
    @Composable
    fun large() = if (LocalConfiguration.current.screenWidthDp < 600) 24.dp else 32.dp
    
    @Composable
    fun extraLarge() = if (LocalConfiguration.current.screenWidthDp < 600) 32.dp else 48.dp
}