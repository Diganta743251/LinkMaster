package com.example.link

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import com.example.link.ui.theme.LinkTheme
import com.example.link.utils.LinkSheetManager

class LinkSheetSettingsActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            LinkTheme {
                LinkSheetSettingsScreen(
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkSheetSettingsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    
    var alwaysAsk by remember { mutableStateOf(LinkSheetManager.shouldAlwaysAsk(context)) }
    var saveBeforeOpen by remember { mutableStateOf(LinkSheetManager.shouldSaveBeforeOpen(context)) }
    var showUsageStats by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showTroubleshootingDialog by remember { mutableStateOf(false) }
    
    val importExportManager = remember { ImportExportManager() }
    
    fun exportLinks() {
        // Implementation will use ImportExportManager
        try {
            importExportManager.exportSettings(context)
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    fun importLinks() {
        // Implementation will use ImportExportManager
        try {
            importExportManager.importSettings(context) { success ->
                // Handle result
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("LinkSheet Settings") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Browser Settings Section
            item {
                SettingsSection(title = "Browser Behavior") {
                    SettingsItem(
                        title = "Always Ask",
                        description = "Show app chooser for every link",
                        icon = Icons.Default.QuestionMark,
                        trailing = {
                            Switch(
                                checked = alwaysAsk,
                                onCheckedChange = { 
                                    alwaysAsk = it
                                    LinkSheetManager.setAlwaysAsk(context, it)
                                }
                            )
                        }
                    )
                    
                    SettingsItem(
                        title = "Save Before Opening",
                        description = "Automatically save links to LinkVault before opening",
                        icon = Icons.Default.BookmarkAdd,
                        trailing = {
                            Switch(
                                checked = saveBeforeOpen,
                                onCheckedChange = { 
                                    saveBeforeOpen = it
                                    LinkSheetManager.setSaveBeforeOpen(context, it)
                                }
                            )
                        }
                    )
                }
            }
            
            // Default Apps Section
            item {
                SettingsSection(title = "Default Apps") {
                    SettingsItem(
                        title = "Set as Default Browser",
                        description = "Make LinkVault your default browser",
                        icon = Icons.Default.Language,
                        onClick = {
                            val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
                            context.startActivity(intent)
                        }
                    )
                    
                    SettingsItem(
                        title = "App Link Settings",
                        description = "Configure which apps open which links",
                        icon = Icons.Default.Link,
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.parse("package:${context.packageName}")
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
            
            // Statistics Section
            item {
                SettingsSection(title = "Usage Statistics") {
                    SettingsItem(
                        title = "View Usage Stats",
                        description = "See which apps you use most for different domains",
                        icon = Icons.Default.Analytics,
                        onClick = { showUsageStats = true }
                    )
                    
                    SettingsItem(
                        title = "Clear Usage Data",
                        description = "Reset all usage statistics and preferences",
                        icon = Icons.Default.ClearAll,
                        onClick = {
                            LinkSheetManager.clearUsageStats(context)
                        }
                    )
                }
            }
            
            // Advanced Section
            item {
                SettingsSection(title = "Advanced") {
                    SettingsItem(
                        title = "Export Settings",
                        description = "Export LinkSheet configuration",
                        icon = Icons.Default.FileDownload,
                        onClick = {
                            exportLinks()
                        }
                    )
                    
                    SettingsItem(
                        title = "Import Settings",
                        description = "Import LinkSheet configuration",
                        icon = Icons.Default.FileUpload,
                        onClick = {
                            importLinks()
                        }
                    )
                }
            }
            
            // Help Section
            item {
                SettingsSection(title = "Help & Support") {
                    SettingsItem(
                        title = "How to Set Default Browser",
                        description = "Step-by-step guide",
                        icon = Icons.Default.Help,
                        onClick = {
                            showHelpDialog = true
                        }
                    )
                    
                    SettingsItem(
                        title = "Troubleshooting",
                        description = "Common issues and solutions",
                        icon = Icons.Default.BugReport,
                        onClick = {
                            showTroubleshootingDialog = true
                        }
                    )
                }
            }
        }
    }
    
    // Usage Stats Dialog
    if (showUsageStats) {
        UsageStatsDialog(
            onDismiss = { showUsageStats = false }
        )
    }
    
    // Help Dialog
    if (showHelpDialog) {
        HelpDialog(
            onDismiss = { showHelpDialog = false }
        )
    }
    
    // Troubleshooting Dialog
    if (showTroubleshootingDialog) {
        TroubleshootingDialog(
            onDismiss = { showTroubleshootingDialog = false }
        )
    }
}



@Composable
fun SettingsItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .let { modifier ->
                if (onClick != null) {
                    modifier.clickable { onClick() }
                } else modifier
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        trailing?.invoke()
    }
}

@Composable
fun UsageStatsDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val usageStats = remember { LinkSheetManager.exportUsageStats(context) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Usage Statistics") },
        text = {
            LazyColumn {
                items(usageStats.entries.toList()) { (key, value) ->
                    if (key.startsWith("usage_")) {
                        val domain = key.substringAfter("usage_").substringBeforeLast("_")
                        val app = key.substringAfterLast("_")
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = domain,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = app,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "$value uses",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
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
fun HelpDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("How to Set Default Browser") },
        text = {
            LazyColumn {
                item {
                    Text(
                        text = """
                        1. Open Android Settings
                        2. Go to Apps & notifications
                        3. Find "Default apps" or "App defaults"
                        4. Select "Browser app"
                        5. Choose LinkSheet as your default browser
                        6. Now all links will open through LinkSheet
                        
                        Alternative method:
                        1. Click any link
                        2. Select LinkSheet from the app chooser
                        3. Tap "Always" instead of "Just once"
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got it")
            }
        }
    )
}

@Composable
fun TroubleshootingDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Troubleshooting") },
        text = {
            LazyColumn {
                item {
                    Text(
                        text = """
                        Common Issues & Solutions:
                        
                        🔗 Links not opening in LinkSheet:
                        • Make sure LinkSheet is set as default browser
                        • Check if the app has link handling permissions
                        • Try clearing default app settings and set again
                        
                        📱 App chooser not showing:
                        • Enable "Always Ask" in settings
                        • Clear defaults for other browser apps
                        • Restart the device if needed
                        
                        ⚡ App crashes or slow performance:
                        • Clear app cache and data
                        • Update to latest version
                        • Restart the device
                        
                        🔒 Links not saving:
                        • Check storage permissions
                        • Ensure "Save Before Open" is enabled
                        • Check available storage space
                        
                        Still having issues? Contact support.
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodyMedium
                    )
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