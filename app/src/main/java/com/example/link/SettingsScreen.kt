package com.example.link

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val biometricManager = remember { LinkVaultBiometricManager(context) }
    
    val settings by settingsManager.allSettings.collectAsState(
        initial = AppSettings(
            biometricEnabled = false,
            autoBackupEnabled = false,
            themeMode = ThemeMode.SYSTEM,
            defaultSortOrder = SortBy.CREATED_DESC,
            showLinkPreviews = true,
            autoGenerateTitles = true,
            clickTrackingEnabled = true,
            exportIncludeMetadata = true,
            lastBackupTime = 0L,
            analyticsEnabled = true
        )
    )
    
    var showThemeDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }
    var showBiometricDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Security Section
            item {
                SettingsSection(title = "Security") {
                    SettingsItem(
                        title = "Biometric Authentication",
                        subtitle = if (settings.biometricEnabled) "Enabled" else "Disabled",
                        icon = Icons.Default.Fingerprint,
                        onClick = { showBiometricDialog = true }
                    ) {
                        Switch(
                            checked = settings.biometricEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled && biometricManager.isBiometricAvailable() == BiometricAvailability.AVAILABLE) {
                                    showBiometricDialog = true
                                } else {
                                    // Disable biometric
                                    // settingsManager.setBiometricEnabled(false)
                                }
                            }
                        )
                    }
                }
            }
            
            // Appearance Section
            item {
                SettingsSection(title = "Appearance") {
                    SettingsItem(
                        title = "Theme",
                        subtitle = when (settings.themeMode) {
                            ThemeMode.LIGHT -> "Light"
                            ThemeMode.DARK -> "Dark"
                            ThemeMode.SYSTEM -> "System Default"
                        },
                        icon = Icons.Default.Palette,
                        onClick = { showThemeDialog = true }
                    )
                    
                    SettingsItem(
                        title = "Show Link Previews",
                        subtitle = "Display website favicons and previews",
                        icon = Icons.Default.Preview
                    ) {
                        Switch(
                            checked = settings.showLinkPreviews,
                            onCheckedChange = { 
                                // settingsManager.setShowLinkPreviews(it)
                            }
                        )
                    }
                }
            }
            
            // Behavior Section
            item {
                SettingsSection(title = "Behavior") {
                    SettingsItem(
                        title = "Default Sort Order",
                        subtitle = when (settings.defaultSortOrder) {
                            SortBy.CREATED_DESC -> "Newest First"
                            SortBy.CREATED_ASC -> "Oldest First"
                            SortBy.TITLE_ASC -> "Title A-Z"
                            SortBy.TITLE_DESC -> "Title Z-A"
                            SortBy.CLICKS_DESC -> "Most Clicked"
                            SortBy.LAST_ACCESSED -> "Recently Accessed"
                        },
                        icon = Icons.Default.Sort,
                        onClick = { showSortDialog = true }
                    )
                    
                    SettingsItem(
                        title = "Auto-Generate Titles",
                        subtitle = "Automatically fetch page titles for new links",
                        icon = Icons.Default.AutoAwesome
                    ) {
                        Switch(
                            checked = settings.autoGenerateTitles,
                            onCheckedChange = { 
                                // settingsManager.setAutoGenerateTitles(it)
                            }
                        )
                    }
                    
                    SettingsItem(
                        title = "Click Tracking",
                        subtitle = "Track how often you click links",
                        icon = Icons.Default.Analytics
                    ) {
                        Switch(
                            checked = settings.clickTrackingEnabled,
                            onCheckedChange = { 
                                // settingsManager.setClickTrackingEnabled(it)
                            }
                        )
                    }
                }
            }
            
            // Backup & Export Section
            item {
                SettingsSection(title = "Backup & Export") {
                    SettingsItem(
                        title = "Auto Backup",
                        subtitle = if (settings.autoBackupEnabled) "Enabled" else "Disabled",
                        icon = Icons.Default.Backup
                    ) {
                        Switch(
                            checked = settings.autoBackupEnabled,
                            onCheckedChange = { 
                                // settingsManager.setAutoBackupEnabled(it)
                            }
                        )
                    }
                    
                    SettingsItem(
                        title = "Include Metadata in Export",
                        subtitle = "Include click counts, timestamps, etc.",
                        icon = Icons.Default.DataObject
                    ) {
                        Switch(
                            checked = settings.exportIncludeMetadata,
                            onCheckedChange = { 
                                // settingsManager.setExportIncludeMetadata(it)
                            }
                        )
                    }
                    
                    if (settings.lastBackupTime > 0) {
                        SettingsItem(
                            title = "Last Backup",
                            subtitle = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
                                .format(java.util.Date(settings.lastBackupTime)),
                            icon = Icons.Default.Schedule
                        )
                    }
                }
            }
            
            // Privacy Section
            item {
                SettingsSection(title = "Privacy") {
                    SettingsItem(
                        title = "Analytics",
                        subtitle = "Help improve the app by sharing usage data",
                        icon = Icons.Default.Analytics
                    ) {
                        Switch(
                            checked = settings.analyticsEnabled,
                            onCheckedChange = { 
                                // settingsManager.setAnalyticsEnabled(it)
                            }
                        )
                    }
                }
            }
            
            // About Section
            item {
                SettingsSection(title = "About") {
                    SettingsItem(
                        title = "Version",
                        subtitle = "1.0.0",
                        icon = Icons.Default.Info
                    )
                    
                    SettingsItem(
                        title = "Privacy Policy",
                        subtitle = "View our privacy policy",
                        icon = Icons.Default.PrivacyTip,
                        onClick = { /* Open privacy policy */ }
                    )
                    
                    SettingsItem(
                        title = "Open Source Licenses",
                        subtitle = "View third-party licenses",
                        icon = Icons.Default.Code,
                        onClick = { /* Open licenses */ }
                    )
                }
            }
        }
    }
    
    // Dialogs
    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = settings.themeMode,
            onThemeSelected = { theme ->
                // settingsManager.setThemeMode(theme)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
    
    if (showSortDialog) {
        SortSelectionDialog(
            currentSort = settings.defaultSortOrder,
            onSortSelected = { sort ->
                // settingsManager.setDefaultSortOrder(sort)
                showSortDialog = false
            },
            onDismiss = { showSortDialog = false }
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = trailing,
        modifier = if (onClick != null) {
            Modifier.clickable { onClick() }
        } else {
            Modifier
        }
    )
}

@Composable
fun ThemeSelectionDialog(
    currentTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Theme") },
        text = {
            Column {
                ThemeMode.values().forEach { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onThemeSelected(theme) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentTheme == theme,
                            onClick = { onThemeSelected(theme) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (theme) {
                                ThemeMode.LIGHT -> "Light"
                                ThemeMode.DARK -> "Dark"
                                ThemeMode.SYSTEM -> "System Default"
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
fun SortSelectionDialog(
    currentSort: SortBy,
    onSortSelected: (SortBy) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Default Sort Order") },
        text = {
            Column {
                SortBy.values().forEach { sort ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSortSelected(sort) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSort == sort,
                            onClick = { onSortSelected(sort) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (sort) {
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