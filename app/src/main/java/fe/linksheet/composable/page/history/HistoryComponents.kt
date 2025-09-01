package fe.linksheet.composable.page.history

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fe.linksheet.composable.ui.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Modern History Components with 2025 UI/UX
 */

/**
 * Modern History List View
 */
@Composable
fun ModernHistoryList(
    items: List<HistoryItem>,
    selectedItems: Set<Int>,
    isSelectionMode: Boolean,
    onItemClick: (HistoryItem) -> Unit,
    onItemLongClick: (HistoryItem) -> Unit,
    onSelectionChanged: (Int, Boolean) -> Unit,
    isCompact: Boolean
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items) { index, item ->
            AnimatedVisibility(
                visible = true,
                enter = ModernAnimations.SlideInFromRight + fadeIn(
                    animationSpec = tween(300, delayMillis = index * 30)
                ),
                exit = ModernAnimations.SlideOutToRight
            ) {
                ModernHistoryListItem(
                    item = item,
                    isSelected = selectedItems.contains(item.id),
                    isSelectionMode = isSelectionMode,
                    onClick = { onItemClick(item) },
                    onLongClick = { onItemLongClick(item) },
                    onSelectionChanged = { selected ->
                        onSelectionChanged(item.id, selected)
                    },
                    isCompact = isCompact
                )
            }
        }
    }
}

/**
 * Modern History List Item
 */
@Composable
private fun ModernHistoryListItem(
    item: HistoryItem,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onSelectionChanged: (Boolean) -> Unit,
    isCompact: Boolean
) {
    val haptic = LocalHapticFeedback.current
    val dateFormatter = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }
    
    SwipeToActionCard(
        onSwipeRight = { /* Copy URL */ },
        onSwipeLeft = { /* Delete */ },
        rightAction = SwipeAction(
            icon = Icons.Default.ContentCopy,
            label = "Copy",
            color = Color(0xFF4CAF50)
        ),
        leftAction = SwipeAction(
            icon = Icons.Default.Delete,
            label = "Delete",
            color = Color(0xFFF44336)
        )
    ) {
        Modern3DCard(
            modifier = Modifier
                .fillMaxWidth()
                .bouncyTap(
                    onTap = {
                        if (isSelectionMode) {
                            onSelectionChanged(!isSelected)
                        } else {
                            onClick()
                        }
                    },
                    onLongPress = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onLongClick()
                    }
                ),
            elevation = if (isSelected) 12.dp else 6.dp,
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(if (isCompact) 16.dp else 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Selection checkbox
                AnimatedVisibility(
                    visible = isSelectionMode,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = onSelectionChanged,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                
                // App icon placeholder
                Box(
                    modifier = Modifier
                        .size(if (isCompact) 40.dp else 48.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.appName.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Content
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.host,
                            style = if (isCompact) {
                                MaterialTheme.typography.titleMedium
                            } else {
                                MaterialTheme.typography.titleLarge
                            }.copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        
                        // Pin indicator
                        if (item.isPinned) {
                            Icon(
                                imageVector = Icons.Default.PushPin,
                                contentDescription = "Pinned",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        
                        // Star indicator
                        if (item.isStarred) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Starred",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.appName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.weight(1f)
                        )
                        
                        Text(
                            text = dateFormatter.format(Date(item.lastUsed)),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                    
                    if (item.visitCount > 1) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${item.visitCount} visits",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modern History Grid View
 */
@Composable
fun ModernHistoryGrid(
    items: List<HistoryItem>,
    selectedItems: Set<Int>,
    isSelectionMode: Boolean,
    onItemClick: (HistoryItem) -> Unit,
    onItemLongClick: (HistoryItem) -> Unit,
    onSelectionChanged: (Int, Boolean) -> Unit,
    columns: Int = 2
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(items) { index, item ->
            AnimatedVisibility(
                visible = true,
                enter = ModernAnimations.ScaleInWithBounce + fadeIn(
                    animationSpec = tween(300, delayMillis = index * 50)
                ),
                exit = ModernAnimations.ScaleOutWithFade
            ) {
                ModernHistoryGridItem(
                    item = item,
                    isSelected = selectedItems.contains(item.id),
                    isSelectionMode = isSelectionMode,
                    onClick = { onItemClick(item) },
                    onLongClick = { onItemLongClick(item) },
                    onSelectionChanged = { selected ->
                        onSelectionChanged(item.id, selected)
                    }
                )
            }
        }
    }
}

/**
 * Modern History Grid Item
 */
@Composable
private fun ModernHistoryGridItem(
    item: HistoryItem,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onSelectionChanged: (Boolean) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val dateFormatter = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }
    
    Modern3DCard(
        modifier = Modifier
            .aspectRatio(1f)
            .bouncyTap(
                onTap = {
                    if (isSelectionMode) {
                        onSelectionChanged(!isSelected)
                    } else {
                        onClick()
                    }
                },
                onLongPress = {
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    onLongClick()
                }
            ),
        elevation = if (isSelected) 12.dp else 6.dp,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Selection checkbox
            AnimatedVisibility(
                visible = isSelectionMode,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = onSelectionChanged,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            
            // Pin/Star indicators
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                if (item.isPinned) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = "Pinned",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                if (item.isStarred) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Starred",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // App icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.appName.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Host
                Text(
                    text = item.host,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // App name
                Text(
                    text = item.appName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Date
                Text(
                    text = dateFormatter.format(Date(item.lastUsed)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
                
                if (item.visitCount > 1) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "${item.visitCount}x",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modern History Timeline View
 */
@Composable
fun ModernHistoryTimeline(
    items: List<HistoryItem>,
    selectedItems: Set<Int>,
    isSelectionMode: Boolean,
    onItemClick: (HistoryItem) -> Unit,
    onItemLongClick: (HistoryItem) -> Unit,
    onSelectionChanged: (Int, Boolean) -> Unit
) {
    val groupedItems = remember(items) {
        items.groupBy { item ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = item.lastUsed
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.timeInMillis
        }.toSortedMap(reverseOrder())
    }
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        groupedItems.forEach { (date, dayItems) ->
            item(key = "header_$date") {
                ModernTimelineHeader(date = date)
            }
            
            itemsIndexed(dayItems) { index, item ->
                AnimatedVisibility(
                    visible = true,
                    enter = ModernAnimations.SlideInFromRight + fadeIn(
                        animationSpec = tween(300, delayMillis = index * 30)
                    ),
                    exit = ModernAnimations.SlideOutToRight
                ) {
                    ModernTimelineItem(
                        item = item,
                        isSelected = selectedItems.contains(item.id),
                        isSelectionMode = isSelectionMode,
                        onClick = { onItemClick(item) },
                        onLongClick = { onItemLongClick(item) },
                        onSelectionChanged = { selected ->
                            onSelectionChanged(item.id, selected)
                        },
                        isLast = index == dayItems.size - 1
                    )
                }
            }
        }
    }
}

/**
 * Modern Timeline Header
 */
@Composable
private fun ModernTimelineHeader(date: Long) {
    val dateFormatter = remember { SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()) }
    val today = remember { Calendar.getInstance().apply { 
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis }
    
    val yesterday = remember { today - 24 * 60 * 60 * 1000 }
    
    val displayText = when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> dateFormatter.format(Date(date))
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = displayText,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(1.dp)
                )
        )
    }
}

/**
 * Modern Timeline Item
 */
@Composable
private fun ModernTimelineItem(
    item: HistoryItem,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onSelectionChanged: (Boolean) -> Unit,
    isLast: Boolean
) {
    val haptic = LocalHapticFeedback.current
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Timeline indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        if (item.isPinned || item.isStarred) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                        CircleShape
                    )
            )
            
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Content
        Modern3DCard(
            modifier = Modifier
                .weight(1f)
                .bouncyTap(
                    onTap = {
                        if (isSelectionMode) {
                            onSelectionChanged(!isSelected)
                        } else {
                            onClick()
                        }
                    },
                    onLongPress = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onLongClick()
                    }
                ),
            elevation = if (isSelected) 8.dp else 4.dp,
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Selection checkbox
                AnimatedVisibility(
                    visible = isSelectionMode,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = onSelectionChanged,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                
                // Time
                Text(
                    text = timeFormatter.format(Date(item.lastUsed)),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width(50.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // App icon
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.appName.take(1).uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Content
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.host,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        
                        if (item.isPinned) {
                            Icon(
                                imageVector = Icons.Default.PushPin,
                                contentDescription = "Pinned",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        
                        if (item.isStarred) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Starred",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    
                    Text(
                        text = item.appName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}