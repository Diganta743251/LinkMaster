package fe.linksheet.composable.page.history

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fe.linksheet.R
import fe.linksheet.composable.ui.*
import fe.linksheet.module.database.entity.AppSelectionHistory
import fe.linksheet.module.viewmodel.HistoryViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Modern History Screen with 2025 UI/UX enhancements
 * Features: Multiple selection, delete, pin, star, search, sort, and responsive design
 */

data class HistoryItem(
    val id: Int,
    val host: String,
    val packageName: String,
    val appName: String,
    val lastUsed: Long,
    val isPinned: Boolean = false,
    val isStarred: Boolean = false,
    val visitCount: Int = 1
)

enum class HistoryViewMode {
    LIST, GRID, TIMELINE
}

enum class HistorySortMode {
    RECENT, OLDEST, ALPHABETICAL, MOST_USED, PINNED_FIRST
}

@Composable
fun HistoryRoute(
    onBackPressed: () -> Unit,
    onNavigateToLinkDetail: (String) -> Unit,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val historyItems by viewModel.historyItems.collectAsStateWithLifecycle()
    val selectedItems by viewModel.selectedItems.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val viewMode by viewModel.viewMode.collectAsStateWithLifecycle()
    val sortMode by viewModel.sortMode.collectAsStateWithLifecycle()
    val isSelectionMode by viewModel.isSelectionMode.collectAsStateWithLifecycle()
    
    val filteredAndSortedItems = remember(historyItems, searchQuery, sortMode) {
        val filtered = if (searchQuery.isEmpty()) {
            historyItems
        } else {
            historyItems.filter { item ->
                item.host.contains(searchQuery, ignoreCase = true) ||
                item.appName.contains(searchQuery, ignoreCase = true)
            }
        }
        
        when (sortMode) {
            HistorySortMode.RECENT -> filtered.sortedByDescending { it.lastUsed }
            HistorySortMode.OLDEST -> filtered.sortedBy { it.lastUsed }
            HistorySortMode.ALPHABETICAL -> filtered.sortedBy { it.host }
            HistorySortMode.MOST_USED -> filtered.sortedByDescending { it.visitCount }
            HistorySortMode.PINNED_FIRST -> filtered.sortedWith(
                compareByDescending<HistoryItem> { it.isPinned }
                    .thenByDescending { it.lastUsed }
            )
        }
    }
    
    AdaptiveLayout(
        compactContent = {
            CompactHistoryScreen(
                historyItems = filteredAndSortedItems,
                selectedItems = selectedItems,
                searchQuery = searchQuery,
                viewMode = viewMode,
                sortMode = sortMode,
                isSelectionMode = isSelectionMode,
                onBackPressed = onBackPressed,
                onSearchQueryChanged = viewModel::updateSearchQuery,
                onViewModeChanged = viewModel::updateViewMode,
                onSortModeChanged = viewModel::updateSortMode,
                onItemClick = { item ->
                    if (isSelectionMode) {
                        viewModel.onItemClick(item)
                    } else {
                        val fullUrl = if (item.host.startsWith("http")) item.host else "https://${item.host}"
                        onNavigateToLinkDetail(fullUrl)
                    }
                },
                onItemLongClick = viewModel::onItemLongClick,
                onSelectionChanged = viewModel::onSelectionChanged,
                onDeleteSelected = viewModel::deleteSelectedItems,
                onPinSelected = viewModel::pinSelectedItems,
                onStarSelected = viewModel::starSelectedItems,
                onClearSelection = viewModel::clearSelection,
                onShareSelected = viewModel::shareSelectedItems
            )
        },
        mediumContent = {
            MediumHistoryScreen(
                historyItems = filteredAndSortedItems,
                selectedItems = selectedItems,
                searchQuery = searchQuery,
                viewMode = viewMode,
                sortMode = sortMode,
                isSelectionMode = isSelectionMode,
                onBackPressed = onBackPressed,
                onSearchQueryChanged = viewModel::updateSearchQuery,
                onViewModeChanged = viewModel::updateViewMode,
                onSortModeChanged = viewModel::updateSortMode,
                onItemClick = { item ->
                    if (isSelectionMode) {
                        viewModel.onItemClick(item)
                    } else {
                        val fullUrl = if (item.host.startsWith("http")) item.host else "https://${item.host}"
                        onNavigateToLinkDetail(fullUrl)
                    }
                },
                onItemLongClick = viewModel::onItemLongClick,
                onSelectionChanged = viewModel::onSelectionChanged,
                onDeleteSelected = viewModel::deleteSelectedItems,
                onPinSelected = viewModel::pinSelectedItems,
                onStarSelected = viewModel::starSelectedItems,
                onClearSelection = viewModel::clearSelection,
                onShareSelected = viewModel::shareSelectedItems
            )
        },
        expandedContent = {
            ExpandedHistoryScreen(
                historyItems = filteredAndSortedItems,
                selectedItems = selectedItems,
                searchQuery = searchQuery,
                viewMode = viewMode,
                sortMode = sortMode,
                isSelectionMode = isSelectionMode,
                onBackPressed = onBackPressed,
                onSearchQueryChanged = viewModel::updateSearchQuery,
                onViewModeChanged = viewModel::updateViewMode,
                onSortModeChanged = viewModel::updateSortMode,
                onItemClick = { item ->
                    if (isSelectionMode) {
                        viewModel.onItemClick(item)
                    } else {
                        val fullUrl = if (item.host.startsWith("http")) item.host else "https://${item.host}"
                        onNavigateToLinkDetail(fullUrl)
                    }
                },
                onItemLongClick = viewModel::onItemLongClick,
                onSelectionChanged = viewModel::onSelectionChanged,
                onDeleteSelected = viewModel::deleteSelectedItems,
                onPinSelected = viewModel::pinSelectedItems,
                onStarSelected = viewModel::starSelectedItems,
                onClearSelection = viewModel::clearSelection,
                onShareSelected = viewModel::shareSelectedItems
            )
        }
    )
}

/**
 * Compact History Screen for phones
 */
@Composable
private fun CompactHistoryScreen(
    historyItems: List<HistoryItem>,
    selectedItems: Set<Int>,
    searchQuery: String,
    viewMode: HistoryViewMode,
    sortMode: HistorySortMode,
    isSelectionMode: Boolean,
    onBackPressed: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onViewModeChanged: (HistoryViewMode) -> Unit,
    onSortModeChanged: (HistorySortMode) -> Unit,
    onItemClick: (HistoryItem) -> Unit,
    onItemLongClick: (HistoryItem) -> Unit,
    onSelectionChanged: (Int, Boolean) -> Unit,
    onDeleteSelected: () -> Unit,
    onPinSelected: () -> Unit,
    onStarSelected: () -> Unit,
    onClearSelection: () -> Unit,
    onShareSelected: () -> Unit
) {
    Column(
        modifier = Modifier
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
        // Modern Header
        ModernHistoryHeader(
            title = "History",
            selectedCount = selectedItems.size,
            isSelectionMode = isSelectionMode,
            onBackPressed = onBackPressed,
            onClearSelection = onClearSelection,
            isCompact = true
        )
        
        // Search and Controls
        ModernHistoryControls(
            searchQuery = searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            viewMode = viewMode,
            onViewModeChanged = onViewModeChanged,
            sortMode = sortMode,
            onSortModeChanged = onSortModeChanged,
            isCompact = true
        )
        
        // History Content
        Box(modifier = Modifier.weight(1f)) {
            when (viewMode) {
                HistoryViewMode.LIST -> {
                    ModernHistoryList(
                        items = historyItems,
                        selectedItems = selectedItems,
                        isSelectionMode = isSelectionMode,
                        onItemClick = onItemClick,
                        onItemLongClick = onItemLongClick,
                        onSelectionChanged = onSelectionChanged,
                        isCompact = true
                    )
                }
                HistoryViewMode.GRID -> {
                    ModernHistoryGrid(
                        items = historyItems,
                        selectedItems = selectedItems,
                        isSelectionMode = isSelectionMode,
                        onItemClick = onItemClick,
                        onItemLongClick = onItemLongClick,
                        onSelectionChanged = onSelectionChanged,
                        columns = 2
                    )
                }
                HistoryViewMode.TIMELINE -> {
                    ModernHistoryTimeline(
                        items = historyItems,
                        selectedItems = selectedItems,
                        isSelectionMode = isSelectionMode,
                        onItemClick = onItemClick,
                        onItemLongClick = onItemLongClick,
                        onSelectionChanged = onSelectionChanged
                    )
                }
            }
        }
        
        // Action Bar
        AnimatedVisibility(
            visible = isSelectionMode && selectedItems.isNotEmpty(),
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(300)
            ) + fadeOut()
        ) {
            ModernHistoryActionBar(
                selectedCount = selectedItems.size,
                onDeleteSelected = onDeleteSelected,
                onPinSelected = onPinSelected,
                onStarSelected = onStarSelected,
                onShareSelected = onShareSelected,
                isCompact = true
            )
        }
    }
}

/**
 * Medium History Screen for tablets
 */
@Composable
private fun MediumHistoryScreen(
    historyItems: List<HistoryItem>,
    selectedItems: Set<Int>,
    searchQuery: String,
    viewMode: HistoryViewMode,
    sortMode: HistorySortMode,
    isSelectionMode: Boolean,
    onBackPressed: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onViewModeChanged: (HistoryViewMode) -> Unit,
    onSortModeChanged: (HistorySortMode) -> Unit,
    onItemClick: (HistoryItem) -> Unit,
    onItemLongClick: (HistoryItem) -> Unit,
    onSelectionChanged: (Int, Boolean) -> Unit,
    onDeleteSelected: () -> Unit,
    onPinSelected: () -> Unit,
    onStarSelected: () -> Unit,
    onClearSelection: () -> Unit,
    onShareSelected: () -> Unit
) {
    Row(
        modifier = Modifier
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
        // Sidebar with controls
        GlassmorphismCard(
            modifier = Modifier
                .width(280.dp)
                .fillMaxHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                ModernHistoryHeader(
                    title = "History",
                    selectedCount = selectedItems.size,
                    isSelectionMode = isSelectionMode,
                    onBackPressed = onBackPressed,
                    onClearSelection = onClearSelection,
                    isCompact = false
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Search
                ModernSearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChanged,
                    onSearch = { },
                    placeholder = "Search history...",
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // View Mode Toggle
                ModernViewModeToggle(
                    currentMode = viewMode,
                    onModeChanged = onViewModeChanged,
                    availableModes = listOf(HistoryViewMode.LIST, HistoryViewMode.GRID, HistoryViewMode.TIMELINE)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Sort Options
                ModernSortOptions(
                    currentSort = sortMode,
                    onSortChanged = onSortModeChanged
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Action Buttons
                if (isSelectionMode && selectedItems.isNotEmpty()) {
                    ModernHistoryActionButtons(
                        selectedCount = selectedItems.size,
                        onDeleteSelected = onDeleteSelected,
                        onPinSelected = onPinSelected,
                        onStarSelected = onStarSelected,
                        onShareSelected = onShareSelected
                    )
                }
            }
        }
        
        // Main content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            when (viewMode) {
                HistoryViewMode.LIST -> {
                    ModernHistoryList(
                        items = historyItems,
                        selectedItems = selectedItems,
                        isSelectionMode = isSelectionMode,
                        onItemClick = onItemClick,
                        onItemLongClick = onItemLongClick,
                        onSelectionChanged = onSelectionChanged,
                        isCompact = false
                    )
                }
                HistoryViewMode.GRID -> {
                    ModernHistoryGrid(
                        items = historyItems,
                        selectedItems = selectedItems,
                        isSelectionMode = isSelectionMode,
                        onItemClick = onItemClick,
                        onItemLongClick = onItemLongClick,
                        onSelectionChanged = onSelectionChanged,
                        columns = 3
                    )
                }
                HistoryViewMode.TIMELINE -> {
                    ModernHistoryTimeline(
                        items = historyItems,
                        selectedItems = selectedItems,
                        isSelectionMode = isSelectionMode,
                        onItemClick = onItemClick,
                        onItemLongClick = onItemLongClick,
                        onSelectionChanged = onSelectionChanged
                    )
                }
            }
        }
    }
}

/**
 * Expanded History Screen for desktop/large tablets
 */
@Composable
private fun ExpandedHistoryScreen(
    historyItems: List<HistoryItem>,
    selectedItems: Set<Int>,
    searchQuery: String,
    viewMode: HistoryViewMode,
    sortMode: HistorySortMode,
    isSelectionMode: Boolean,
    onBackPressed: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onViewModeChanged: (HistoryViewMode) -> Unit,
    onSortModeChanged: (HistorySortMode) -> Unit,
    onItemClick: (HistoryItem) -> Unit,
    onItemLongClick: (HistoryItem) -> Unit,
    onSelectionChanged: (Int, Boolean) -> Unit,
    onDeleteSelected: () -> Unit,
    onPinSelected: () -> Unit,
    onStarSelected: () -> Unit,
    onClearSelection: () -> Unit,
    onShareSelected: () -> Unit
) {
    Column(
        modifier = Modifier
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
        // Top Bar with all controls
        GlassmorphismCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Title and selection info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = if (isSelectionMode) {
                            "${selectedItems.size} selected"
                        } else {
                            "History"
                        },
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    
                    if (isSelectionMode) {
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(onClick = onClearSelection) {
                            Text("Clear Selection")
                        }
                    }
                }
                
                // Search bar
                ModernSearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChanged,
                    onSearch = { },
                    placeholder = "Search history...",
                    modifier = Modifier.width(300.dp)
                )
                
                // Controls
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ModernViewModeToggle(
                        currentMode = viewMode,
                        onModeChanged = onViewModeChanged,
                        availableModes = listOf(HistoryViewMode.LIST, HistoryViewMode.GRID, HistoryViewMode.TIMELINE)
                    )
                    
                    ModernSortButton(
                        currentSort = sortMode,
                        onSortChanged = onSortModeChanged
                    )
                    
                    if (isSelectionMode && selectedItems.isNotEmpty()) {
                        ModernHistoryActionButtons(
                            selectedCount = selectedItems.size,
                            onDeleteSelected = onDeleteSelected,
                            onPinSelected = onPinSelected,
                            onStarSelected = onStarSelected,
                            onShareSelected = onShareSelected
                        )
                    }
                }
            }
        }
        
        // Main content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            when (viewMode) {
                HistoryViewMode.LIST -> {
                    ModernHistoryList(
                        items = historyItems,
                        selectedItems = selectedItems,
                        isSelectionMode = isSelectionMode,
                        onItemClick = onItemClick,
                        onItemLongClick = onItemLongClick,
                        onSelectionChanged = onSelectionChanged,
                        isCompact = false
                    )
                }
                HistoryViewMode.GRID -> {
                    ModernHistoryGrid(
                        items = historyItems,
                        selectedItems = selectedItems,
                        isSelectionMode = isSelectionMode,
                        onItemClick = onItemClick,
                        onItemLongClick = onItemLongClick,
                        onSelectionChanged = onSelectionChanged,
                        columns = 4
                    )
                }
                HistoryViewMode.TIMELINE -> {
                    ModernHistoryTimeline(
                        items = historyItems,
                        selectedItems = selectedItems,
                        isSelectionMode = isSelectionMode,
                        onItemClick = onItemClick,
                        onItemLongClick = onItemLongClick,
                        onSelectionChanged = onSelectionChanged
                    )
                }
            }
        }
    }
}

/**
 * Modern History Header
 */
@Composable
private fun ModernHistoryHeader(
    title: String,
    selectedCount: Int,
    isSelectionMode: Boolean,
    onBackPressed: () -> Unit,
    onClearSelection: () -> Unit,
    isCompact: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (isCompact) 16.dp else 0.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackPressed) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (isSelectionMode) "$selectedCount selected" else title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    brush = if (!isSelectionMode) {
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    } else null
                )
            )
            
            if (!isSelectionMode) {
                Text(
                    text = "Your link opening history",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        
        if (isSelectionMode) {
            TextButton(onClick = onClearSelection) {
                Text("Clear")
            }
        }
    }
}

/**
 * Modern History Controls
 */
@Composable
private fun ModernHistoryControls(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    viewMode: HistoryViewMode,
    onViewModeChanged: (HistoryViewMode) -> Unit,
    sortMode: HistorySortMode,
    onSortModeChanged: (HistorySortMode) -> Unit,
    isCompact: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Search Bar
        ModernSearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChanged,
            onSearch = { },
            placeholder = "Search history...",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )
        
        // View and Sort Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ModernViewModeToggle(
                currentMode = viewMode,
                onModeChanged = onViewModeChanged,
                availableModes = if (isCompact) {
                    listOf(HistoryViewMode.LIST, HistoryViewMode.GRID)
                } else {
                    listOf(HistoryViewMode.LIST, HistoryViewMode.GRID, HistoryViewMode.TIMELINE)
                }
            )
            
            ModernSortButton(
                currentSort = sortMode,
                onSortChanged = onSortModeChanged
            )
        }
    }
}