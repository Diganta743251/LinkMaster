package fe.linksheet.composable.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlin.math.*

/**
 * Modern Gesture and Interaction Enhancements for 2025
 * Advanced touch interactions, haptic feedback, and gesture recognition
 */

/**
 * Advanced Swipe-to-Action Component
 */
@Composable
fun SwipeToActionCard(
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    leftAction: SwipeAction? = null,
    rightAction: SwipeAction? = null,
    threshold: Float = 0.3f,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    var isSwipeActive by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "swipe_offset"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        isSwipeActive = true
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    },
                    onDragEnd = {
                        val swipeDistance = abs(offsetX) / size.width
                        
                        when {
                            offsetX > size.width * threshold && onSwipeRight != null -> {
                                onSwipeRight()
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            }
                            offsetX < -size.width * threshold && onSwipeLeft != null -> {
                                onSwipeLeft()
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            }
                        }
                        
                        offsetX = 0f
                        isSwipeActive = false
                    }
                ) { _, dragAmount ->
                    offsetX = (offsetX + dragAmount).coerceIn(-size.width.toFloat(), size.width.toFloat())
                }
            }
    ) {
        // Background actions
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Right swipe action (appears on left)
            rightAction?.let { action ->
                SwipeActionBackground(
                    action = action,
                    isVisible = offsetX > 0,
                    progress = (offsetX / (size.width * threshold)).coerceIn(0f, 1f),
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(120.dp)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Left swipe action (appears on right)
            leftAction?.let { action ->
                SwipeActionBackground(
                    action = action,
                    isVisible = offsetX < 0,
                    progress = (abs(offsetX) / (size.width * threshold)).coerceIn(0f, 1f),
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(120.dp)
                )
            }
        }
        
        // Main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .graphicsLayer {
                    val scale = 1f - (abs(animatedOffsetX) / size.width * 0.05f)
                    scaleX = scale
                    scaleY = scale
                }
        ) {
            content()
        }
    }
}

data class SwipeAction(
    val icon: ImageVector,
    val label: String,
    val color: Color,
    val contentColor: Color = Color.White
)

@Composable
private fun SwipeActionBackground(
    action: SwipeAction,
    isVisible: Boolean,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) progress else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "action_scale"
    )
    
    Box(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        action.color.copy(alpha = 0.8f),
                        action.color
                    )
                )
            )
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.label,
                tint = action.contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = action.label,
                style = MaterialTheme.typography.labelSmall,
                color = action.contentColor
            )
        }
    }
}

/**
 * Pull-to-Refresh with modern animations
 */
@Composable
fun ModernPullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    refreshThreshold: Float = 120f,
    content: @Composable () -> Unit
) {
    var pullOffset by remember { mutableStateOf(0f) }
    var isRefreshTriggered by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    
    val pullProgress = (pullOffset / refreshThreshold).coerceIn(0f, 1f)
    val rotation by animateFloatAsState(
        targetValue = pullProgress * 360f,
        animationSpec = tween(300),
        label = "refresh_rotation"
    )
    
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            pullOffset = 0f
            isRefreshTriggered = false
        }
    }
    
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = {
                        if (!isRefreshing) {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        }
                    },
                    onDragEnd = {
                        if (pullOffset >= refreshThreshold && !isRefreshing) {
                            isRefreshTriggered = true
                            onRefresh()
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        } else {
                            pullOffset = 0f
                        }
                    }
                ) { _, dragAmount ->
                    if (!isRefreshing && dragAmount > 0) {
                        pullOffset = (pullOffset + dragAmount * 0.5f).coerceAtMost(refreshThreshold * 1.5f)
                    }
                }
            }
    ) {
        // Refresh indicator
        AnimatedVisibility(
            visible = pullOffset > 0 || isRefreshing,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1f)
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isRefreshing) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Pull to refresh",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(rotation)
                    )
                }
            }
        }
        
        // Content with offset
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, pullOffset.roundToInt()) }
        ) {
            content()
        }
    }
}

/**
 * Long Press with Contextual Menu
 */
@Composable
fun LongPressContextMenu(
    menuItems: List<ContextMenuItem>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var menuPosition by remember { mutableStateOf(Offset.Zero) }
    val haptic = LocalHapticFeedback.current
    
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset ->
                        menuPosition = offset
                        showMenu = true
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    }
                )
            }
    ) {
        content()
        
        if (showMenu) {
            ContextMenuOverlay(
                items = menuItems,
                position = menuPosition,
                onDismiss = {
                    showMenu = false
                    onDismiss()
                }
            )
        }
    }
}

data class ContextMenuItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit,
    val destructive: Boolean = false
)

@Composable
private fun ContextMenuOverlay(
    items: List<ContextMenuItem>,
    position: Offset,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onDismiss() }
    ) {
        GlassmorphismCard(
            modifier = Modifier
                .offset {
                    IntOffset(
                        position.x.roundToInt(),
                        position.y.roundToInt()
                    )
                }
                .animateContentSize(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                items.forEach { item ->
                    ContextMenuItemRow(
                        item = item,
                        onClick = {
                            item.onClick()
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ContextMenuItemRow(
    item: ContextMenuItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = if (item.destructive) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = item.label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (item.destructive) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

/**
 * Pinch-to-Zoom Container
 */
@Composable
fun PinchToZoomContainer(
    modifier: Modifier = Modifier,
    minScale: Float = 0.5f,
    maxScale: Float = 3f,
    content: @Composable () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(minScale, maxScale)
                    offset += pan
                }
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }
    ) {
        content()
    }
}

/**
 * Drag and Drop Container
 */
@Composable
fun DragAndDropContainer(
    onDrop: (Offset) -> Unit,
    modifier: Modifier = Modifier,
    dragContent: @Composable () -> Unit,
    dropZoneContent: @Composable () -> Unit
) {
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    
    Box(modifier = modifier) {
        // Drop zone
        dropZoneContent()
        
        // Draggable content
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        dragOffset.x.roundToInt(),
                        dragOffset.y.roundToInt()
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        },
                        onDragEnd = {
                            onDrop(dragOffset)
                            dragOffset = Offset.Zero
                            isDragging = false
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        }
                    ) { _, dragAmount ->
                        dragOffset += dragAmount
                    }
                }
                .graphicsLayer {
                    val scale = if (isDragging) 1.1f else 1f
                    scaleX = scale
                    scaleY = scale
                    alpha = if (isDragging) 0.8f else 1f
                }
        ) {
            dragContent()
        }
    }
}

/**
 * Multi-Touch Gesture Detector
 */
@Composable
fun MultiTouchGestureDetector(
    onSingleTap: () -> Unit = {},
    onDoubleTap: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onTwoFingerTap: () -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onSingleTap()
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    },
                    onDoubleTap = {
                        onDoubleTap()
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    },
                    onLongPress = {
                        onLongPress()
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    }
                )
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.changes.size == 2 && event.changes.all { it.pressed }) {
                            onTwoFingerTap()
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        }
                    }
                }
            }
    ) {
        content()
    }
}