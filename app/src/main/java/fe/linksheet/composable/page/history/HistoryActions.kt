package fe.linksheet.composable.page.history

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fe.linksheet.composable.ui.*

/**
 * Modern History Action Components with 2025 UI/UX
 */

/**
 * Modern View Mode Toggle for History
 */
@Composable
fun ModernViewModeToggle(
    currentMode: HistoryViewMode,
    onModeChanged: (HistoryViewMode) -> Unit,
    availableModes: List<HistoryViewMode>
) {
    Row(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        availableModes.forEach { mode ->
            val isSelected = mode == currentMode
            
            Modern3DButton(
                onClick = { onModeChanged(mode) },
                modifier = Modifier
                    .size(40.dp)
                    .bouncyTap(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    contentColor = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            ) {
                Icon(
                    imageVector = when (mode) {
                        HistoryViewMode.LIST -> Icons.Default.List
                        HistoryViewMode.GRID -> Icons.Default.GridView
                        HistoryViewMode.TIMELINE -> Icons.Default.Timeline
                    },
                    contentDescription = mode.name,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Modern Sort Button for History
 */
@Composable
fun ModernSortButton(
    currentSort: HistorySortMode,
    onSortChanged: (HistorySortMode) -> Unit
) {
    var showSortMenu by remember { mutableStateOf(false) }
    
    Box {
        Modern3DButton(
            onClick = { showSortMenu = true },
            modifier = Modifier
                .size(40.dp)
                .bouncyTap(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Icon(
                imageVector = Icons.Default.Sort,
                contentDescription = "Sort",
                modifier = Modifier.size(20.dp)
            )
        }
        
        DropdownMenu(
            expanded = showSortMenu,
            onDismissRequest = { showSortMenu = false }
        ) {
            HistorySortMode.values().forEach { mode ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = when (mode) {
                                HistorySortMode.RECENT -> "Most Recent"
                                HistorySortMode.OLDEST -> "Oldest First"
                                HistorySortMode.ALPHABETICAL -> "Alphabetical"
                                HistorySortMode.MOST_USED -> "Most Used"
                                HistorySortMode.PINNED_FIRST -> "Pinned First"
                            }
                        )
                    },
                    onClick = {
                        onSortChanged(mode)
                        showSortMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = when (mode) {
                                HistorySortMode.RECENT -> Icons.Default.Schedule
                                HistorySortMode.OLDEST -> Icons.Default.History
                                HistorySortMode.ALPHABETICAL -> Icons.Default.SortByAlpha
                                HistorySortMode.MOST_USED -> Icons.Default.TrendingUp
                                HistorySortMode.PINNED_FIRST -> Icons.Default.PushPin
                            },
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (mode == currentSort) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}

/**
 * Modern Sort Options Panel
 */
@Composable
fun ModernSortOptions(
    currentSort: HistorySortMode,
    onSortChanged: (HistorySortMode) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Sort by",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        
        HistorySortMode.values().forEach { mode ->
            val isSelected = mode == currentSort
            
            Modern3DCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .bouncyTap(onTap = { onSortChanged(mode) }),
                elevation = if (isSelected) 6.dp else 2.dp,
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (mode) {
                            HistorySortMode.RECENT -> Icons.Default.Schedule
                            HistorySortMode.OLDEST -> Icons.Default.History
                            HistorySortMode.ALPHABETICAL -> Icons.Default.SortByAlpha
                            HistorySortMode.MOST_USED -> Icons.Default.TrendingUp
                            HistorySortMode.PINNED_FIRST -> Icons.Default.PushPin
                        },
                        contentDescription = null,
                        tint = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.size(20.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = when (mode) {
                            HistorySortMode.RECENT -> "Most Recent"
                            HistorySortMode.OLDEST -> "Oldest First"
                            HistorySortMode.ALPHABETICAL -> "Alphabetical"
                            HistorySortMode.MOST_USED -> "Most Used"
                            HistorySortMode.PINNED_FIRST -> "Pinned First"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modern History Action Bar (Bottom)
 */
@Composable
fun ModernHistoryActionBar(
    selectedCount: Int,
    onDeleteSelected: () -> Unit,
    onPinSelected: () -> Unit,
    onStarSelected: () -> Unit,
    onShareSelected: () -> Unit,
    isCompact: Boolean
) {
    GlassmorphismCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isCompact) 16.dp else 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ModernActionButton(
                icon = Icons.Default.PushPin,
                label = "Pin",
                onClick = onPinSelected,
                color = MaterialTheme.colorScheme.primary
            )
            
            ModernActionButton(
                icon = Icons.Default.Star,
                label = "Star",
                onClick = onStarSelected,
                color = Color(0xFFFFD700)
            )
            
            ModernActionButton(
                icon = Icons.Default.Share,
                label = "Share",
                onClick = onShareSelected,
                color = MaterialTheme.colorScheme.secondary
            )
            
            ModernActionButton(
                icon = Icons.Default.Delete,
                label = "Delete",
                onClick = onDeleteSelected,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * Modern History Action Buttons (Sidebar)
 */
@Composable
fun ModernHistoryActionButtons(
    selectedCount: Int,
    onDeleteSelected: () -> Unit,
    onPinSelected: () -> Unit,
    onStarSelected: () -> Unit,
    onShareSelected: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "$selectedCount selected",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        
        Modern3DButton(
            onClick = onPinSelected,
            modifier = Modifier
                .fillMaxWidth()
                .bouncyTap(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.PushPin,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pin")
        }
        
        Modern3DButton(
            onClick = onStarSelected,
            modifier = Modifier
                .fillMaxWidth()
                .bouncyTap(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700),
                contentColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Star")
        }
        
        Modern3DButton(
            onClick = onShareSelected,
            modifier = Modifier
                .fillMaxWidth()
                .bouncyTap(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Share")
        }
        
        Modern3DButton(
            onClick = onDeleteSelected,
            modifier = Modifier
                .fillMaxWidth()
                .bouncyTap(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Delete")
        }
    }
}

/**
 * Modern Action Button
 */
@Composable
private fun ModernActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    color: Color
) {
    val haptic = LocalHapticFeedback.current
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.bouncyTap(
            onTap = {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                onClick()
            }
        )
    ) {
        Modern3DButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = color.copy(alpha = 0.1f),
                contentColor = color
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Modern History Stats Card
 */
@Composable
fun ModernHistoryStatsCard(
    totalItems: Int,
    pinnedItems: Int,
    starredItems: Int,
    todayItems: Int,
    modifier: Modifier = Modifier
) {
    Modern3DCard(
        modifier = modifier.fillMaxWidth(),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "History Stats",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = Icons.Default.History,
                    label = "Total",
                    value = totalItems.toString(),
                    color = MaterialTheme.colorScheme.primary
                )
                
                StatItem(
                    icon = Icons.Default.Today,
                    label = "Today",
                    value = todayItems.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = Icons.Default.PushPin,
                    label = "Pinned",
                    value = pinnedItems.toString(),
                    color = MaterialTheme.colorScheme.tertiary
                )
                
                StatItem(
                    icon = Icons.Default.Star,
                    label = "Starred",
                    value = starredItems.toString(),
                    color = Color(0xFFFFD700)
                )
            }
        }
    }
}

/**
 * Stat Item Component
 */
@Composable
private fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}