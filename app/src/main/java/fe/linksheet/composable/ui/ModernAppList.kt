package fe.linksheet.composable.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Modern 2025 App List Components with enhanced UI/UX
 */

data class ModernAppItem(
    val id: String,
    val name: String,
    val packageName: String,
    val icon: @Composable () -> Unit,
    val isSelected: Boolean = false,
    val isRecommended: Boolean = false,
    val category: String? = null,
    val description: String? = null
)

/**
 * Modern App Grid with enhanced visual design
 */
@Composable
fun ModernAppGrid(
    apps: List<ModernAppItem>,
    onAppClick: (ModernAppItem) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    showSearch: Boolean = true
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredApps = remember(apps, searchQuery) {
        if (searchQuery.isEmpty()) apps
        else apps.filter { 
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.packageName.contains(searchQuery, ignoreCase = true)
        }
    }
    
    Column(modifier = modifier) {
        if (showSearch) {
            ModernSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { },
                placeholder = "Search apps...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
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
                        onClick = { onAppClick(app) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * Modern App Grid Item with 3D effects
 */
@Composable
private fun ModernAppGridItem(
    app: ModernAppItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardModifier = if (app.isRecommended) {
        modifier.then(
            Modifier
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
        )
    } else {
        modifier
    }
    
    Modern3DCard(
        modifier = cardModifier
            .aspectRatio(1f)
            .bouncyTap(onTap = onClick),
        elevation = if (app.isSelected) 12.dp else 6.dp,
        colors = CardDefaults.cardColors(
            containerColor = when {
                app.isSelected -> MaterialTheme.colorScheme.primaryContainer
                app.isRecommended -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surface
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
            // App icon with modern styling
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (app.isSelected || app.isRecommended) {
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(Color.Transparent, Color.Transparent)
                            )
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                app.icon()
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // App name
            Text(
                text = app.name,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = when {
                    app.isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                    app.isRecommended -> MaterialTheme.colorScheme.onSecondaryContainer
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
            
            // Selection indicator
            AnimatedVisibility(
                visible = app.isSelected,
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
            
            // Recommended badge
            AnimatedVisibility(
                visible = app.isRecommended && !app.isSelected,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Surface(
                    modifier = Modifier.padding(top = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.tertiary
                ) {
                    Text(
                        text = "★",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * Modern App List with enhanced styling
 */
@Composable
fun ModernAppList(
    apps: List<ModernAppItem>,
    onAppClick: (ModernAppItem) -> Unit,
    modifier: Modifier = Modifier,
    showSearch: Boolean = true,
    groupByCategory: Boolean = false
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredApps = remember(apps, searchQuery) {
        if (searchQuery.isEmpty()) apps
        else apps.filter { 
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.packageName.contains(searchQuery, ignoreCase = true)
        }
    }
    
    val groupedApps = remember(filteredApps, groupByCategory) {
        if (groupByCategory) {
            filteredApps.groupBy { it.category ?: "Other" }
        } else {
            mapOf("All Apps" to filteredApps)
        }
    }
    
    Column(modifier = modifier) {
        if (showSearch) {
            ModernSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { },
                placeholder = "Search apps...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedApps.forEach { (category, categoryApps) ->
                if (groupByCategory && groupedApps.size > 1) {
                    item(key = "category_$category") {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
                
                itemsIndexed(
                    items = categoryApps,
                    key = { _, app -> app.id }
                ) { index, app ->
                    AnimatedVisibility(
                        visible = true,
                        enter = ModernAnimations.SlideInFromRight + fadeIn(
                            animationSpec = tween(300, delayMillis = index * 30)
                        ),
                        exit = ModernAnimations.SlideOutToRight
                    ) {
                        ModernAppCard(
                            appName = app.name,
                            packageName = app.packageName,
                            icon = app.icon,
                            onClick = { onAppClick(app) },
                            isSelected = app.isSelected,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modern App Selection Bottom Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernAppSelectionBottomSheet(
    apps: List<ModernAppItem>,
    onAppSelected: (ModernAppItem) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Select App",
    showAsGrid: Boolean = false
) {
    ModernBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        // Header
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
            
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }
        
        // App list/grid
        if (showAsGrid) {
            ModernAppGrid(
                apps = apps,
                onAppClick = onAppSelected,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            ModernAppList(
                apps = apps,
                onAppClick = onAppSelected,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}