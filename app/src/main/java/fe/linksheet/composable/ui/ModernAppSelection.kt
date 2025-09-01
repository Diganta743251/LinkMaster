package fe.linksheet.composable.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import fe.linksheet.feature.app.ActivityAppInfo

/**
 * Modern 2025 App Selection Screen with enhanced UX
 */

data class ModernAppSelectionState(
    val apps: List<ActivityAppInfo>,
    val selectedApp: ActivityAppInfo? = null,
    val searchQuery: String = "",
    val viewMode: AppViewMode = AppViewMode.LIST,
    val sortMode: AppSortMode = AppSortMode.NAME,
    val showOnlyRecommended: Boolean = false,
    val categories: List<String> = emptyList()
)

enum class AppViewMode {
    LIST, GRID, COMPACT
}

enum class AppSortMode {
    NAME, RECENT, RECOMMENDED, CATEGORY
}

/**
 * Main Modern App Selection Screen
 */
@Composable
fun ModernAppSelectionScreen(
    state: ModernAppSelectionState,
    onAppSelected: (ActivityAppInfo) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onViewModeChanged: (AppViewMode) -> Unit,
    onSortModeChanged: (AppSortMode) -> Unit,
    onFilterToggled: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isCompact = configuration.screenWidthDp < 600
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    )
                )
            )
    ) {
        // Modern Header with Search and Controls
        ModernAppSelectionHeader(
            searchQuery = state.searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            viewMode = state.viewMode,
            onViewModeChanged = onViewModeChanged,
            sortMode = state.sortMode,
            onSortModeChanged = onSortModeChanged,
            showOnlyRecommended = state.showOnlyRecommended,
            onFilterToggled = onFilterToggled,
            isCompact = isCompact
        )
        
        // App List/Grid with modern animations
        ModernAppList(
            apps = state.apps,
            selectedApp = state.selectedApp,
            onAppSelected = onAppSelected,
            viewMode = state.viewMode,
            searchQuery = state.searchQuery,
            modifier = Modifier.weight(1f)
        )
        
        // Modern Action Bar
        if (state.selectedApp != null) {
            ModernActionBar(
                selectedApp = state.selectedApp,
                onAppSelected = onAppSelected,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Modern Header with Search and Controls
 */
@Composable
private fun ModernAppSelectionHeader(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    viewMode: AppViewMode,
    onViewModeChanged: (AppViewMode) -> Unit,
    sortMode: AppSortMode,
    onSortModeChanged: (AppSortMode) -> Unit,
    showOnlyRecommended: Boolean,
    onFilterToggled: () -> Unit,
    isCompact: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Title with modern styling
        Text(
            text = "Choose App",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Search Bar
        ModernSearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChanged,
            onSearch = { },
            placeholder = "Search apps...",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )
        
        // Control Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // View Mode Toggle
            ModernViewModeToggle(
                currentMode = viewMode,
                onModeChanged = onViewModeChanged,
                isCompact = isCompact
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Sort Button
                ModernSortButton(
                    currentSort = sortMode,
                    onSortChanged = onSortModeChanged
                )
                
                // Filter Button
                ModernFilterButton(
                    isActive = showOnlyRecommended,
                    onClick = onFilterToggled
                )
            }
        }
    }
}

/**
 * Modern View Mode Toggle
 */
@Composable
private fun ModernViewModeToggle(
    currentMode: AppViewMode,
    onModeChanged: (AppViewMode) -> Unit,
    isCompact: Boolean
) {
    val modes = if (isCompact) {
        listOf(AppViewMode.LIST, AppViewMode.GRID)
    } else {
        AppViewMode.values().toList()
    }
    
    Row(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        modes.forEach { mode ->
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
                        AppViewMode.LIST -> Icons.Default.List
                        AppViewMode.GRID -> Icons.Default.GridView
                        AppViewMode.COMPACT -> Icons.Default.ViewCompact
                    },
                    contentDescription = mode.name,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Modern Sort Button
 */
@Composable
private fun ModernSortButton(
    currentSort: AppSortMode,
    onSortChanged: (AppSortMode) -> Unit
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
            AppSortMode.values().forEach { mode ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = when (mode) {
                                AppSortMode.NAME -> "Name"
                                AppSortMode.RECENT -> "Recent"
                                AppSortMode.RECOMMENDED -> "Recommended"
                                AppSortMode.CATEGORY -> "Category"
                            }
                        )
                    },
                    onClick = {
                        onSortChanged(mode)
                        showSortMenu = false
                    },
                    leadingIcon = {
                        if (mode == currentSort) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}

/**
 * Modern Filter Button
 */
@Composable
private fun ModernFilterButton(
    isActive: Boolean,
    onClick: () -> Unit
) {
    Modern3DButton(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp)
            .bouncyTap(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isActive) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    ) {
        Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = "Filter",
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * Modern App List with adaptive layout
 */
@Composable
private fun ModernAppList(
    apps: List<ActivityAppInfo>,
    selectedApp: ActivityAppInfo?,
    onAppSelected: (ActivityAppInfo) -> Unit,
    viewMode: AppViewMode,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    val filteredApps = remember(apps, searchQuery) {
        if (searchQuery.isEmpty()) {
            apps
        } else {
            apps.filter { app ->
                app.label.contains(searchQuery, ignoreCase = true) ||
                app.packageName.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    when (viewMode) {
        AppViewMode.LIST -> {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(filteredApps) { index, app ->
                    AnimatedVisibility(
                        visible = true,
                        enter = ModernAnimations.SlideInFromRight + fadeIn(
                            animationSpec = tween(300, delayMillis = index * 30)
                        ),
                        exit = ModernAnimations.SlideOutToRight
                    ) {
                        ModernAppListItem(
                            app = app,
                            isSelected = app == selectedApp,
                            onClick = { onAppSelected(app) }
                        )
                    }
                }
            }
        }
        
        AppViewMode.GRID -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),
                modifier = modifier,
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(filteredApps) { index, app ->
                    AnimatedVisibility(
                        visible = true,
                        enter = ModernAnimations.ScaleInWithBounce + fadeIn(
                            animationSpec = tween(300, delayMillis = index * 50)
                        ),
                        exit = ModernAnimations.ScaleOutWithFade
                    ) {
                        ModernAppGridItem(
                            app = app,
                            isSelected = app == selectedApp,
                            onClick = { onAppSelected(app) }
                        )
                    }
                }
            }
        }
        
        AppViewMode.COMPACT -> {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(filteredApps) { index, app ->
                    AnimatedVisibility(
                        visible = true,
                        enter = ModernAnimations.SlideInFromRight + fadeIn(
                            animationSpec = tween(300, delayMillis = index * 20)
                        ),
                        exit = ModernAnimations.SlideOutToRight
                    ) {
                        ModernAppCompactItem(
                            app = app,
                            isSelected = app == selectedApp,
                            onClick = { onAppSelected(app) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modern App List Item
 */
@Composable
private fun ModernAppListItem(
    app: ActivityAppInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    SwipeToActionCard(
        onSwipeRight = { /* Add to favorites */ },
        onSwipeLeft = { /* Hide app */ },
        rightAction = SwipeAction(
            icon = Icons.Default.Favorite,
            label = "Favorite",
            color = Color(0xFF4CAF50)
        ),
        leftAction = SwipeAction(
            icon = Icons.Default.VisibilityOff,
            label = "Hide",
            color = Color(0xFFF44336)
        )
    ) {
        ModernAppCard(
            appName = app.label,
            packageName = app.packageName,
            icon = {
                // App icon placeholder - replace with actual icon loading
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = app.label.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            onClick = onClick,
            isSelected = isSelected,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Modern App Grid Item
 */
@Composable
private fun ModernAppGridItem(
    app: ActivityAppInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Modern3DCard(
        modifier = Modifier
            .aspectRatio(1f)
            .bouncyTap(onTap = onClick),
        elevation = if (isSelected) 12.dp else 6.dp,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
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
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        } else {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        },
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = app.label.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // App name
            Text(
                text = app.label,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
            
            // Selection indicator
            AnimatedVisibility(
                visible = isSelected,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(top = 4.dp)
                )
            }
        }
    }
}

/**
 * Modern App Compact Item
 */
@Composable
private fun ModernAppCompactItem(
    app: ActivityAppInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                } else {
                    Color.Transparent
                }
            )
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
                text = app.label.take(1).uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // App name
        Text(
            text = app.label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        
        // Selection indicator
        AnimatedVisibility(
            visible = isSelected,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Modern Action Bar
 */
@Composable
private fun ModernActionBar(
    selectedApp: ActivityAppInfo,
    onAppSelected: (ActivityAppInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    
    GlassmorphismCard(
        modifier = modifier
            .padding(16.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Selected app info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedApp.label.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = selectedApp.label,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "Selected",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Open button
                Modern3DButton(
                    onClick = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onAppSelected(selectedApp)
                    },
                    modifier = Modifier.bouncyTap(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Launch,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open")
                }
            }
        }
    }
}