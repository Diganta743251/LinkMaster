package com.example.link

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            LinkVaultTheme {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    composable("main") {
                        LinkVaultApp(
                            onNavigateToSettings = {
                                navController.navigate("settings")
                            },
                            onNavigateToStatistics = {
                                navController.navigate("statistics")
                            }
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("statistics") {
                        StatisticsScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
        
        // Handle intent from shared URLs
        handleIntent(intent)
    }
    
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(it) }
    }
    
    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SEND -> {
                if (intent.type == "text/plain") {
                    val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                    val sharedTitle = intent.getStringExtra(Intent.EXTRA_SUBJECT) ?: ""
                    
                    if (!sharedText.isNullOrEmpty()) {
                        saveSharedLink(sharedText, sharedTitle)
                    }
                }
            }
        }
    }
    
    private fun saveSharedLink(url: String, title: String) {
        lifecycleScope.launch {
            try {
                val database = AppDatabase.getDatabase(this@MainActivity)
                val linkDao = database.linkDao()
                
                // Check if link already exists
                val existingLink = linkDao.getLinkByUrl(url)
                if (existingLink != null) {
                    Toast.makeText(this@MainActivity, "Link already exists in LinkVault", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                
                val link = Link(
                    url = url,
                    title = title.ifEmpty { "Shared Link" },
                    tags = "shared",
                    description = "",
                    notes = "Shared to LinkVault",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                
                linkDao.insert(link)
                Toast.makeText(this@MainActivity, "Link saved successfully", Toast.LENGTH_LONG).show()
                
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error saving link: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun LinkVaultTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = dynamicColorScheme(),
        content = content
    )
}

@Composable
private fun dynamicColorScheme(): ColorScheme {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        dynamicDarkColorScheme(LocalContext.current)
    } else {
        darkColorScheme()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun LinkVaultApp(
    onNavigateToSettings: () -> Unit = {},
    onNavigateToStatistics: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: LinkViewModel = viewModel()
    val links by viewModel.links.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val linksCount by viewModel.linksCount.collectAsState()
    val favoriteLinksCount by viewModel.favoriteLinksCount.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var showMainMenu by remember { mutableStateOf(false) }
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedLinks by remember { mutableStateOf(setOf<Int>()) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Handle back button in selection mode
    BackHandler(enabled = isSelectionMode) {
        isSelectionMode = false
        selectedLinks = emptySet()
    }
    
    // Handle error messages
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }
    
    Scaffold(
        topBar = {
            AnimatedTopAppBar(
                title = "LinkVault",
                subtitle = "$linksCount links • $favoriteLinksCount favorites",
                showSearchBar = showSearchBar,
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::setSearchQuery,
                onSearchToggle = { showSearchBar = !showSearchBar },
                onSortClick = { showSortMenu = true },
                onSettingsClick = onNavigateToSettings,
                onStatisticsClick = onNavigateToStatistics,
                onExportClick = { viewModel.exportLinks(context) },
                onImportClick = { viewModel.importLinks(context) }
            )
        },
        floatingActionButton = {
            if (isSelectionMode && selectedLinks.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = {
                        val linksToDelete = links.filter { it.id in selectedLinks }
                        viewModel.deleteLinks(linksToDelete)
                        selectedLinks = emptySet()
                        isSelectionMode = false
                    },
                    icon = { Icon(Icons.Default.Delete, contentDescription = null) },
                    text = { Text("Delete ${selectedLinks.size}") },
                    containerColor = MaterialTheme.colorScheme.error
                )
            } else {
                AnimatedFloatingActionButton(
                    onClick = { showAddDialog = true },
                    isLoading = isLoading
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isLoading,
            onRefresh = { viewModel.refreshLinks() }
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            val context = LocalContext.current
            
            if (links.isEmpty() && !isLoading) {
                EmptyState(
                    onAddClick = { showAddDialog = true },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                AnimatedLinksList(
                    links = links,
                    onLinkClick = { link ->
                        if (isSelectionMode) {
                            selectedLinks = if (link.id in selectedLinks) {
                                selectedLinks - link.id
                            } else {
                                selectedLinks + link.id
                            }
                        } else {
                            openLink(context, link.url)
                            viewModel.incrementClicks(link.id)
                        }
                    },
                    onLongClick = { link ->
                        if (!isSelectionMode) {
                            isSelectionMode = true
                            selectedLinks = setOf(link.id)
                        }
                    },
                    onFavoriteClick = viewModel::toggleFavorite,
                    onDeleteClick = viewModel::deleteLink,
                    isSelectionMode = isSelectionMode,
                    selectedLinks = selectedLinks,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
    
    // Dialogs
    if (showAddDialog) {
        AnimatedAddLinkDialog(
            onDismiss = { showAddDialog = false },
            onAddLink = { url, title, tags ->
                viewModel.insertLink(
                    Link(
                        url = url,
                        title = title,
                        tags = tags,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
                showAddDialog = false
            }
        )
    }
    
    if (showSortMenu) {
        SortMenuDialog(
            currentSort = viewModel.sortBy.collectAsState().value,
            onSortSelected = { sortBy ->
                viewModel.setSortBy(sortBy)
                showSortMenu = false
            },
            onDismiss = { showSortMenu = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AnimatedTopAppBar(
    title: String,
    subtitle: String,
    showSearchBar: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchToggle: () -> Unit,
    onSortClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    onStatisticsClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onImportClick: () -> Unit = {}
) {
    var showMainMenu by remember { mutableStateOf(false) }
    AnimatedContent(
        targetState = showSearchBar,
        transitionSpec = {
            slideInVertically { -it } + fadeIn() with
            slideOutVertically { -it } + fadeOut()
        }
    ) { isSearching ->
        if (isSearching) {
            SearchTopAppBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                onCloseClick = onSearchToggle
            )
        } else {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchToggle) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onSortClick) {
                        Icon(Icons.Default.Sort, contentDescription = "Sort")
                    }
                    Box {
                        IconButton(onClick = { showMainMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options")
                        }
                        DropdownMenu(
                            expanded = showMainMenu,
                            onDismissRequest = { showMainMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Statistics") },
                                onClick = {
                                    showMainMenu = false
                                    onStatisticsClick()
                                },
                                leadingIcon = { Icon(Icons.Default.Analytics, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Export Links") },
                                onClick = {
                                    showMainMenu = false
                                    onExportClick()
                                },
                                leadingIcon = { Icon(Icons.Default.FileDownload, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Import Links") },
                                onClick = {
                                    showMainMenu = false
                                    onImportClick()
                                },
                                leadingIcon = { Icon(Icons.Default.FileUpload, contentDescription = null) }
                            )
                            Divider()
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = {
                                    showMainMenu = false
                                    onSettingsClick()
                                },
                                leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) }
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onCloseClick: () -> Unit
) {
    TopAppBar(
        title = {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search links, titles, tags...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = if (query.isNotEmpty()) {
                    {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                } else null
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Close search")
            }
        }
    )
}

@Composable
fun AnimatedFloatingActionButton(
    onClick: () -> Unit,
    isLoading: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isLoading) 0.8f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.scale(scale)
    ) {
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                scaleIn() + fadeIn() with scaleOut() + fadeOut()
            }
        ) { loading ->
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Default.Add, contentDescription = "Add Link")
            }
        }
    }
}

@Composable
fun AnimatedLinksList(
    links: List<Link>,
    onLinkClick: (Link) -> Unit,
    onLongClick: (Link) -> Unit = {},
    onFavoriteClick: (Link) -> Unit,
    onDeleteClick: (Link) -> Unit,
    isSelectionMode: Boolean = false,
    selectedLinks: Set<Int> = emptySet(),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = links,
            key = { it.id }
        ) { link ->
            AnimatedLinkItem(
                link = link,
                onLinkClick = { onLinkClick(link) },
                onLongClick = { onLongClick(link) },
                onFavoriteClick = { onFavoriteClick(link) },
                onDeleteClick = { onDeleteClick(link) },
                isSelectionMode = isSelectionMode,
                isSelected = link.id in selectedLinks,
                modifier = Modifier.animateItemPlacement(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AnimatedLinkItem(
    link: Link,
    onLinkClick: () -> Unit,
    onLongClick: () -> Unit = {},
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onLinkClick,
                onLongClick = onLongClick
            ),
        colors = if (isSelected) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Selection indicator or favicon
                    if (isSelectionMode) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { onLinkClick() },
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        AsyncImage(
                            model = "https://www.google.com/s2/favicons?domain=${Uri.parse(link.url).host}&sz=32",
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = link.title.ifEmpty { "Untitled" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = link.url,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                
                Row {
                    // Favorite button
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (link.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (link.isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (link.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // More options
                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "More options"
                        )
                    }
                }
            }
            
            // Expandable content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    if (link.description.isNotEmpty()) {
                        Text(
                            text = link.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    
                    if (link.tags.isNotEmpty()) {
                        Text(
                            text = "Tags: ${link.tags}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Clicks: ${link.clicks}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Row {
                            val context = LocalContext.current
                            val clipboardManager = LocalClipboardManager.current
                            
                            // Copy URL
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(link.url))
                                    Toast.makeText(context, "URL copied to clipboard", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Copy URL",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            
                            // Share
                            IconButton(
                                onClick = {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "${link.title}\n${link.url}")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Link"))
                                }
                            ) {
                                Icon(
                                    Icons.Default.Share,
                                    contentDescription = "Share",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            
                            // Delete
                            IconButton(onClick = onDeleteClick) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedAddLinkDialog(
    onDismiss: () -> Unit,
    onAddLink: (String, String, String) -> Unit
) {
    var url by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var urlError by remember { mutableStateOf<String?>(null) }
    var isLoadingTitle by remember { mutableStateOf(false) }
    val viewModel: LinkViewModel = viewModel()
    
    fun validateUrl(input: String): String? {
        return when {
            input.isBlank() -> "URL is required"
            !input.startsWith("http://") && !input.startsWith("https://") -> {
                if (input.contains(".") && input.length > 3) null else "Please enter a valid URL"
            }
            input.startsWith("http://") || input.startsWith("https://") -> {
                try {
                    java.net.URL(input)
                    null
                } catch (e: Exception) {
                    "Invalid URL format"
                }
            }
            else -> null
        }
    }
    
    fun formatUrl(input: String): String {
        return if (!input.startsWith("http://") && !input.startsWith("https://")) {
            "https://$input"
        } else {
            input
        }
    }
    
    // Auto-fetch title when URL changes
    LaunchedEffect(url) {
        if (url.isNotBlank() && urlError == null && title.isBlank() && url.length > 5) {
            kotlinx.coroutines.delay(1000) // Debounce
            isLoadingTitle = true
            val formattedUrl = formatUrl(url.trim())
            try {
                val fetchedTitle = viewModel.fetchTitleFromUrl(formattedUrl)
                if (fetchedTitle != null && fetchedTitle.isNotBlank()) {
                    title = fetchedTitle
                }
            } catch (e: Exception) {
                // Silent fail for title fetching
            } finally {
                isLoadingTitle = false
            }
        }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Link") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = url,
                    onValueChange = { 
                        url = it
                        urlError = validateUrl(it)
                    },
                    label = { Text("URL *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = urlError != null,
                    supportingText = urlError?.let { { Text(it) } },
                    leadingIcon = {
                        Icon(Icons.Default.Link, contentDescription = null)
                    }
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Title, contentDescription = null)
                    },
                    trailingIcon = if (isLoadingTitle) {
                        {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    } else null,
                    placeholder = { Text("Auto-fetched from URL") }
                )
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (comma-separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Tag, contentDescription = null)
                    },
                    placeholder = { Text("work, important, read-later") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val finalUrl = formatUrl(url.trim())
                    if (url.isNotBlank() && urlError == null) {
                        onAddLink(finalUrl, title.trim(), tags.trim())
                    }
                },
                enabled = url.isNotBlank() && urlError == null
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SortMenuDialog(
    currentSort: SortBy,
    onSortSelected: (SortBy) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sort Links") },
        text = {
            Column {
                SortBy.values().forEach { sortBy ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSort == sortBy,
                            onClick = { onSortSelected(sortBy) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (sortBy) {
                                SortBy.CREATED_DESC -> "Newest First"
                                SortBy.CREATED_ASC -> "Oldest First"
                                SortBy.TITLE_ASC -> "Title A-Z"
                                SortBy.TITLE_DESC -> "Title Z-A"
                                SortBy.CLICKS_DESC -> "Most Clicked"
                                SortBy.LAST_ACCESSED -> "Recently Accessed"
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun EmptyState(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Link,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "No links yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Add your first link to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onAddClick,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Link")
        }
    }
}

fun openLink(context: android.content.Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Cannot open link", Toast.LENGTH_SHORT).show()
    }
}