package com.example.link

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {
    
    companion object {
        private val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        private val AUTO_BACKUP_ENABLED = booleanPreferencesKey("auto_backup_enabled")
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val DEFAULT_SORT_ORDER = stringPreferencesKey("default_sort_order")
        private val SHOW_LINK_PREVIEWS = booleanPreferencesKey("show_link_previews")
        private val AUTO_GENERATE_TITLES = booleanPreferencesKey("auto_generate_titles")
        private val CLICK_TRACKING_ENABLED = booleanPreferencesKey("click_tracking_enabled")
        private val EXPORT_INCLUDE_METADATA = booleanPreferencesKey("export_include_metadata")
        private val LAST_BACKUP_TIME = longPreferencesKey("last_backup_time")
        private val ANALYTICS_ENABLED = booleanPreferencesKey("analytics_enabled")
    }
    
    val allSettings: Flow<AppSettings> = context.dataStore.data.map { preferences ->
        AppSettings(
            biometricEnabled = preferences[BIOMETRIC_ENABLED] ?: false,
            autoBackupEnabled = preferences[AUTO_BACKUP_ENABLED] ?: false,
            themeMode = ThemeMode.valueOf(preferences[THEME_MODE] ?: ThemeMode.SYSTEM.name),
            defaultSortOrder = SortBy.valueOf(preferences[DEFAULT_SORT_ORDER] ?: SortBy.CREATED_DESC.name),
            showLinkPreviews = preferences[SHOW_LINK_PREVIEWS] ?: true,
            autoGenerateTitles = preferences[AUTO_GENERATE_TITLES] ?: true,
            clickTrackingEnabled = preferences[CLICK_TRACKING_ENABLED] ?: true,
            exportIncludeMetadata = preferences[EXPORT_INCLUDE_METADATA] ?: true,
            lastBackupTime = preferences[LAST_BACKUP_TIME] ?: 0L,
            analyticsEnabled = preferences[ANALYTICS_ENABLED] ?: true
        )
    }
    
    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BIOMETRIC_ENABLED] = enabled
        }
    }
    
    suspend fun setAutoBackupEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_BACKUP_ENABLED] = enabled
        }
    }
    
    suspend fun setThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = themeMode.name
        }
    }
    
    suspend fun setDefaultSortOrder(sortBy: SortBy) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_SORT_ORDER] = sortBy.name
        }
    }
    
    suspend fun setShowLinkPreviews(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_LINK_PREVIEWS] = enabled
        }
    }
    
    suspend fun setAutoGenerateTitles(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_GENERATE_TITLES] = enabled
        }
    }
    
    suspend fun setClickTrackingEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[CLICK_TRACKING_ENABLED] = enabled
        }
    }
    
    suspend fun setExportIncludeMetadata(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[EXPORT_INCLUDE_METADATA] = enabled
        }
    }
    
    suspend fun setLastBackupTime(timestamp: Long) {
        context.dataStore.edit { preferences ->
            preferences[LAST_BACKUP_TIME] = timestamp
        }
    }
    
    suspend fun setAnalyticsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ANALYTICS_ENABLED] = enabled
        }
    }
}

data class AppSettings(
    val biometricEnabled: Boolean,
    val autoBackupEnabled: Boolean,
    val themeMode: ThemeMode,
    val defaultSortOrder: SortBy,
    val showLinkPreviews: Boolean,
    val autoGenerateTitles: Boolean,
    val clickTrackingEnabled: Boolean,
    val exportIncludeMetadata: Boolean,
    val lastBackupTime: Long,
    val analyticsEnabled: Boolean
)

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}