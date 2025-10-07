package com.example.link

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.lifecycle.lifecycleScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import com.example.link.ui.theme.LinkTheme
import com.example.link.utils.*
import kotlinx.coroutines.launch

class LinkInterceptorActivity : ComponentActivity() {
    
    private lateinit var database: AppDatabase
    private lateinit var linkDao: LinkDao
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        database = AppDatabase.getDatabase(this)
        linkDao = database.linkDao()
        
        val url = intent.dataString ?: intent.getStringExtra(Intent.EXTRA_TEXT)
        
        if (url.isNullOrEmpty()) {
            Toast.makeText(this, "No URL provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        setContent {
            LinkTheme {
                LinkInterceptorScreen(
                    url = url,
                    onAppSelected = { appChoice ->
                        handleAppSelection(url, appChoice)
                    },
                    onSaveAndOpen = { appChoice ->
                        handleSaveAndOpen(url, appChoice)
                    },
                    onSaveOnly = {
                        handleSaveOnly(url)
                    },
                    onCancel = {
                        finish()
                    }
                )
            }
        }
    }
    
    private fun handleAppSelection(url: String, appChoice: AppChoice) {
        val success = LinkSheetManager.openWithApp(this, url, appChoice)
        if (!success) {
            Toast.makeText(this, "Failed to open with ${appChoice.appName}", Toast.LENGTH_SHORT).show()
        }
        finish()
    }
    
    private fun handleSaveAndOpen(url: String, appChoice: AppChoice) {
        lifecycleScope.launch {
            try {
                // Save the link first
                val category = SmartCategorizer.categorizeLink(url)
                val link = Link(
                    url = url,
                    title = "Loading...", // Will be updated by metadata fetch
                    tags = category.suggestedTags.joinToString(", "),
                    description = "",
                    notes = "Auto-saved from link interceptor",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                
                linkDao.insert(link)
                
                // Then open with selected app
                val success = LinkSheetManager.openWithApp(this@LinkInterceptorActivity, url, appChoice)
                if (!success) {
                    Toast.makeText(this@LinkInterceptorActivity, "Failed to open with ${appChoice.appName}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LinkInterceptorActivity, "Link saved and opened", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LinkInterceptorActivity, "Error saving link: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
    
    private fun handleSaveOnly(url: String) {
        lifecycleScope.launch {
            try {
                val category = SmartCategorizer.categorizeLink(url)
                val link = Link(
                    url = url,
                    title = "Loading...",
                    tags = category.suggestedTags.joinToString(", "),
                    description = "",
                    notes = "Saved from link interceptor",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                
                linkDao.insert(link)
                Toast.makeText(this@LinkInterceptorActivity, "Link saved to LinkVault", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@LinkInterceptorActivity, "Error saving link: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkInterceptorScreen(
    url: String,
    onAppSelected: (AppChoice) -> Unit,
    onSaveAndOpen: (AppChoice) -> Unit,
    onSaveOnly: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    var apps by remember { mutableStateOf<List<AppChoice>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf("All") }
    var showSaveOptions by remember { mutableStateOf(false) }
    var linkCategory by remember { mutableStateOf<LinkCategory?>(null) }
    
    LaunchedEffect(url) {
        apps = LinkSheetManager.getAppsForUrl(context, url)
        linkCategory = SmartCategorizer.categorizeLink(url)
    }
    
    val categories = remember(apps) {
        listOf("All") + LinkSheetManager.getAppCategories(apps)
    }
    
    val filteredApps = remember(apps, selectedCategory) {
        if (selectedCategory == "All") apps
        else LinkSheetManager.filterAppsByCategory(apps, selectedCategory)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Open Link") },
            navigationIcon = {
                IconButton(onClick = onCancel) {
                    Icon(Icons.Default.Close, contentDescription = "Cancel")
                }
            },
            actions = {
                IconButton(onClick = { showSaveOptions = !showSaveOptions }) {
                    Icon(
                        if (showSaveOptions) Icons.Default.BookmarkAdded else Icons.Default.BookmarkBorder,
                        contentDescription = "Save options"
                    )
                }
            }
        )
        
        // URL Preview Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = linkCategory?.icon ?: "🔗",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Uri.parse(url).host ?: url,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = url,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                
                linkCategory?.let { category ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AssistChip(
                            onClick = { },
                            label = { Text(category.name) },
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            Color(android.graphics.Color.parseColor(category.color)),
                                            CircleShape
                                        )
                                )
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${(category.confidence * 100).toInt()}% confidence",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // Save Options (if enabled)
        if (showSaveOptions) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Save Options",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onSaveOnly,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Bookmark, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save Only")
                        }
                        
                        Button(
                            onClick = { 
                                if (filteredApps.isNotEmpty()) {
                                    onSaveAndOpen(filteredApps.first())
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save & Open")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        // Category Filter
        if (categories.size > 1) {
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        selected = selectedCategory == category
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        // Apps List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredApps) { app ->
                AppChoiceCard(
                    app = app,
                    onAppClick = { onAppSelected(app) },
                    onSaveAndOpen = { onSaveAndOpen(app) },
                    showSaveOption = showSaveOptions
                )
            }
        }
    }
}

@Composable
fun AppChoiceCard(
    app: AppChoice,
    onAppClick: () -> Unit,
    onSaveAndOpen: () -> Unit,
    showSaveOption: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAppClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Icon
            app.icon?.let { icon ->
                Image(
                    bitmap = icon.toBitmap(48, 48).asImageBitmap(),
                    contentDescription = app.appName,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } ?: Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Apps,
                    contentDescription = app.appName,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // App Info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = app.appName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    if (app.isRecommended) {
                        Spacer(modifier = Modifier.width(8.dp))
                        AssistChip(
                            onClick = { },
                            label = { Text("Recommended") },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
                
                Text(
                    text = app.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (app.confidence > 0) {
                    Text(
                        text = "${(app.confidence * 100).toInt()}% match",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            // Action Buttons
            if (showSaveOption) {
                IconButton(onClick = onSaveAndOpen) {
                    Icon(
                        Icons.Default.BookmarkAdd,
                        contentDescription = "Save and open",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            IconButton(onClick = onAppClick) {
                Icon(
                    Icons.Default.Launch,
                    contentDescription = "Open",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}