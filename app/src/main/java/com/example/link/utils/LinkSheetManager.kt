package com.example.link.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat

data class AppChoice(
    val packageName: String,
    val activityName: String,
    val appName: String,
    val icon: android.graphics.drawable.Drawable?,
    val isRecommended: Boolean = false,
    val confidence: Float = 0.0f,
    val category: String = "Browser"
)

object LinkSheetManager {
    
    private const val PREF_NAME = "linksheet_preferences"
    private const val PREF_DEFAULT_APPS = "default_apps"
    private const val PREF_ALWAYS_ASK = "always_ask"
    private const val PREF_SAVE_BEFORE_OPEN = "save_before_open"
    
    /**
     * Get all apps that can handle a URL
     */
    fun getAppsForUrl(context: Context, url: String): List<AppChoice> {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val packageManager = context.packageManager
        
        val resolveInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        }
        
        val apps = mutableListOf<AppChoice>()
        val smartSuggestions = SmartCategorizer.suggestApps(url, context)
        
        resolveInfos.forEach { resolveInfo ->
            val packageName = resolveInfo.activityInfo.packageName
            val activityName = resolveInfo.activityInfo.name
            val appName = resolveInfo.loadLabel(packageManager).toString()
            val icon = resolveInfo.loadIcon(packageManager)
            
            // Check if this app is in smart suggestions
            val suggestion = smartSuggestions.find { it.packageName == packageName }
            val isRecommended = suggestion != null && suggestion.confidence > 0.7f
            val confidence = suggestion?.confidence ?: 0.0f
            
            apps.add(
                AppChoice(
                    packageName = packageName,
                    activityName = activityName,
                    appName = appName,
                    icon = icon,
                    isRecommended = isRecommended,
                    confidence = confidence,
                    category = categorizeApp(packageName, appName)
                )
            )
        }
        
        // Sort by recommendation and confidence
        return apps.sortedWith(compareByDescending<AppChoice> { it.isRecommended }
            .thenByDescending { it.confidence }
            .thenBy { it.appName })
    }
    
    /**
     * Open URL with specific app
     */
    fun openWithApp(context: Context, url: String, appChoice: AppChoice): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                setClassName(appChoice.packageName, appChoice.activityName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            
            // Record usage for learning
            recordAppUsage(context, url, appChoice.packageName)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Open URL with system default or show chooser
     */
    fun openUrl(context: Context, url: String, showChooser: Boolean = false): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            if (showChooser) {
                val chooserIntent = Intent.createChooser(intent, "Open with")
                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooserIntent)
            } else {
                context.startActivity(intent)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Set default app for a domain
     */
    fun setDefaultApp(context: Context, domain: String, packageName: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val defaultApps = prefs.getStringSet(PREF_DEFAULT_APPS, mutableSetOf()) ?: mutableSetOf()
        
        // Remove existing entry for this domain
        defaultApps.removeAll { it.startsWith("$domain:") }
        
        // Add new entry
        defaultApps.add("$domain:$packageName")
        
        prefs.edit().putStringSet(PREF_DEFAULT_APPS, defaultApps).apply()
    }
    
    /**
     * Get default app for a domain
     */
    fun getDefaultApp(context: Context, domain: String): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val defaultApps = prefs.getStringSet(PREF_DEFAULT_APPS, emptySet()) ?: emptySet()
        
        return defaultApps.find { it.startsWith("$domain:") }?.substringAfter(":")
    }
    
    /**
     * Check if should always ask for app choice
     */
    fun shouldAlwaysAsk(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(PREF_ALWAYS_ASK, true)
    }
    
    /**
     * Set always ask preference
     */
    fun setAlwaysAsk(context: Context, alwaysAsk: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(PREF_ALWAYS_ASK, alwaysAsk).apply()
    }
    
    /**
     * Check if should save link before opening
     */
    fun shouldSaveBeforeOpen(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(PREF_SAVE_BEFORE_OPEN, false)
    }
    
    /**
     * Set save before open preference
     */
    fun setSaveBeforeOpen(context: Context, saveBeforeOpen: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(PREF_SAVE_BEFORE_OPEN, saveBeforeOpen).apply()
    }
    
    /**
     * Record app usage for learning user preferences
     */
    private fun recordAppUsage(context: Context, url: String, packageName: String) {
        val prefs = context.getSharedPreferences("app_usage", Context.MODE_PRIVATE)
        val domain = Uri.parse(url).host ?: return
        
        val usageKey = "usage_${domain}_$packageName"
        val currentCount = prefs.getInt(usageKey, 0)
        prefs.edit().putInt(usageKey, currentCount + 1).apply()
        
        // Also record timestamp
        val timestampKey = "timestamp_${domain}_$packageName"
        prefs.edit().putLong(timestampKey, System.currentTimeMillis()).apply()
    }
    
    /**
     * Get usage statistics for learning
     */
    fun getUsageStats(context: Context, domain: String): Map<String, Int> {
        val prefs = context.getSharedPreferences("app_usage", Context.MODE_PRIVATE)
        val stats = mutableMapOf<String, Int>()
        
        prefs.all.forEach { (key, value) ->
            if (key.startsWith("usage_$domain") && value is Int) {
                val packageName = key.substringAfterLast("_")
                stats[packageName] = value
            }
        }
        
        return stats
    }
    
    /**
     * Get recommended app based on usage history
     */
    fun getRecommendedApp(context: Context, url: String): String? {
        val domain = Uri.parse(url).host ?: return null
        val usageStats = getUsageStats(context, domain)
        
        return usageStats.maxByOrNull { it.value }?.key
    }
    
    /**
     * Categorize app by package name
     */
    private fun categorizeApp(packageName: String, appName: String): String {
        return when {
            packageName.contains("chrome") || packageName.contains("firefox") || 
            packageName.contains("browser") || packageName.contains("opera") -> "Browser"
            
            packageName.contains("facebook") || packageName.contains("instagram") ||
            packageName.contains("twitter") || packageName.contains("linkedin") -> "Social"
            
            packageName.contains("youtube") || packageName.contains("netflix") ||
            packageName.contains("twitch") || packageName.contains("video") -> "Video"
            
            packageName.contains("spotify") || packageName.contains("music") ||
            packageName.contains("soundcloud") -> "Music"
            
            packageName.contains("amazon") || packageName.contains("ebay") ||
            packageName.contains("shop") -> "Shopping"
            
            packageName.contains("github") || packageName.contains("stackoverflow") -> "Development"
            
            packageName.contains("maps") || packageName.contains("uber") ||
            packageName.contains("lyft") -> "Navigation"
            
            packageName.contains("news") || packageName.contains("cnn") ||
            packageName.contains("bbc") -> "News"
            
            else -> "Other"
        }
    }
    
    /**
     * Get app categories for filtering
     */
    fun getAppCategories(apps: List<AppChoice>): List<String> {
        return apps.map { it.category }.distinct().sorted()
    }
    
    /**
     * Filter apps by category
     */
    fun filterAppsByCategory(apps: List<AppChoice>, category: String): List<AppChoice> {
        return apps.filter { it.category == category }
    }
    
    /**
     * Check if app is a browser
     */
    fun isBrowser(packageName: String): Boolean {
        val browserPackages = listOf(
            "com.android.chrome", "com.chrome.beta", "com.chrome.dev", "com.chrome.canary",
            "org.mozilla.firefox", "org.mozilla.firefox_beta", "org.mozilla.fenix",
            "com.microsoft.emmx", "com.opera.browser", "com.opera.browser.beta",
            "com.brave.browser", "com.duckduckgo.mobile.android", "com.samsung.android.app.sbrowser",
            "com.UCMobile.intl", "mark.via.gp", "com.kiwibrowser.browser",
            "org.chromium.chrome", "com.sec.android.app.sbrowser"
        )
        
        return browserPackages.any { packageName.contains(it) }
    }
    
    /**
     * Get browser apps only
     */
    fun getBrowserApps(context: Context): List<AppChoice> {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com"))
        val packageManager = context.packageManager
        
        val resolveInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        }
        
        return resolveInfos.filter { isBrowser(it.activityInfo.packageName) }
            .map { resolveInfo ->
                AppChoice(
                    packageName = resolveInfo.activityInfo.packageName,
                    activityName = resolveInfo.activityInfo.name,
                    appName = resolveInfo.loadLabel(packageManager).toString(),
                    icon = resolveInfo.loadIcon(packageManager),
                    category = "Browser"
                )
            }
    }
    
    /**
     * Clear usage statistics
     */
    fun clearUsageStats(context: Context) {
        val prefs = context.getSharedPreferences("app_usage", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
    
    /**
     * Export usage statistics
     */
    fun exportUsageStats(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences("app_usage", Context.MODE_PRIVATE)
        val stats = mutableMapOf<String, Any>()
        
        prefs.all.forEach { (key, value) ->
            value?.let { stats[key] = it }
        }
        
        return stats
    }
}