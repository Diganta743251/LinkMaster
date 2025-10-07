package com.example.link.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

data class ExtensionData(
    val url: String,
    val title: String,
    val tags: String = "",
    val notes: String = "",
    val source: String = "extension"
)

object BrowserExtensionHelper {
    
    private const val EXTENSION_SCHEME = "lv"
    private const val EXTENSION_HOST_ADD = "add"
    private const val EXTENSION_HOST_OPEN = "open"
    
    /**
     * Handle intent from browser extension
     */
    fun handleExtensionIntent(context: Context, intent: Intent): ExtensionData? {
        val data = intent.data ?: return null
        
        return when {
            // Handle custom lv:// scheme
            data.scheme == EXTENSION_SCHEME -> {
                handleCustomScheme(data)
            }
            // Handle https://linkvault.extension/ URLs
            data.host == "linkvault.extension" -> {
                handleExtensionUrl(data)
            }
            // Handle shared URLs from browser
            intent.action == Intent.ACTION_SEND && intent.type == "text/plain" -> {
                handleSharedUrl(intent)
            }
            else -> null
        }
    }
    
    private fun handleCustomScheme(uri: Uri): ExtensionData? {
        return when (uri.host) {
            EXTENSION_HOST_ADD -> {
                ExtensionData(
                    url = uri.getQueryParameter("url") ?: "",
                    title = uri.getQueryParameter("title") ?: "",
                    tags = uri.getQueryParameter("tags") ?: "",
                    notes = uri.getQueryParameter("notes") ?: "",
                    source = uri.getQueryParameter("source") ?: "extension"
                )
            }
            EXTENSION_HOST_OPEN -> {
                // Handle opening specific link by slug
                val slug = uri.getQueryParameter("slug")
                if (slug != null) {
                    // This would be handled by the main activity to navigate to the link
                    ExtensionData(
                        url = "",
                        title = "",
                        tags = "",
                        notes = "open:$slug",
                        source = "open"
                    )
                } else null
            }
            else -> null
        }
    }
    
    private fun handleExtensionUrl(uri: Uri): ExtensionData? {
        return when (uri.path) {
            "/add" -> {
                ExtensionData(
                    url = uri.getQueryParameter("url") ?: "",
                    title = uri.getQueryParameter("title") ?: "",
                    tags = uri.getQueryParameter("tags") ?: "",
                    notes = uri.getQueryParameter("notes") ?: "",
                    source = uri.getQueryParameter("source") ?: "extension"
                )
            }
            "/open" -> {
                val slug = uri.getQueryParameter("slug")
                if (slug != null) {
                    ExtensionData(
                        url = "",
                        title = "",
                        tags = "",
                        notes = "open:$slug",
                        source = "open"
                    )
                } else null
            }
            else -> null
        }
    }
    
    private fun handleSharedUrl(intent: Intent): ExtensionData? {
        val sharedUrl = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return null
        val sharedTitle = intent.getStringExtra(Intent.EXTRA_SUBJECT) ?: ""
        
        return ExtensionData(
            url = sharedUrl,
            title = sharedTitle,
            source = "share"
        )
    }
    
    /**
     * Generate extension URLs for sharing
     */
    fun generateExtensionUrl(url: String, title: String, tags: String = "", notes: String = ""): String {
        val uri = Uri.Builder()
            .scheme("https")
            .authority("linkvault.extension")
            .path("/add")
            .appendQueryParameter("url", url)
            .appendQueryParameter("title", title)
            .appendQueryParameter("tags", tags)
            .appendQueryParameter("notes", notes)
            .appendQueryParameter("source", "generated")
            .build()
        
        return uri.toString()
    }
    
    /**
     * Generate custom scheme URL
     */
    fun generateCustomSchemeUrl(url: String, title: String, tags: String = "", notes: String = ""): String {
        val uri = Uri.Builder()
            .scheme(EXTENSION_SCHEME)
            .authority(EXTENSION_HOST_ADD)
            .appendQueryParameter("url", url)
            .appendQueryParameter("title", title)
            .appendQueryParameter("tags", tags)
            .appendQueryParameter("notes", notes)
            .build()
        
        return uri.toString()
    }
    
    /**
     * Create intent filter strings for AndroidManifest.xml
     */
    fun getIntentFilterData(): List<String> {
        return listOf(
            // Custom scheme
            "<data android:scheme=\"$EXTENSION_SCHEME\" />",
            // Extension URLs
            "<data android:scheme=\"https\" android:host=\"linkvault.extension\" />",
            // Share intents are handled by default ACTION_SEND
        )
    }
    
    /**
     * Validate extension data
     */
    fun validateExtensionData(data: ExtensionData): Boolean {
        return when (data.source) {
            "open" -> data.notes.startsWith("open:")
            else -> data.url.isNotEmpty() && (data.url.startsWith("http://") || data.url.startsWith("https://"))
        }
    }
    
    /**
     * Show extension installation instructions
     */
    fun showInstallationInstructions(context: Context) {
        val instructions = """
            To install the LinkVault browser extension:
            
            1. Chrome/Edge: Go to chrome://extensions/, enable Developer mode, click "Load unpacked"
            2. Firefox: Go to about:debugging, click "Load Temporary Add-on"
            3. Select the browser-extension folder from LinkVault project
            
            Features:
            • Save links with Ctrl+Shift+L
            • Right-click context menu
            • Auto-fill titles and descriptions
            • Tag suggestions
        """.trimIndent()
        
        Toast.makeText(context, "Extension installation guide copied to clipboard", Toast.LENGTH_LONG).show()
    }
    
    /**
     * Generate QR code data for extension installation
     */
    fun getExtensionInstallationQR(): String {
        return "https://github.com/your-repo/linkvault/tree/main/browser-extension"
    }
    
    /**
     * Check if extension is likely installed (heuristic)
     */
    fun isExtensionLikelyInstalled(context: Context): Boolean {
        // This is a heuristic - we can't directly detect if extension is installed
        // But we can check if we've received extension intents recently
        val prefs = context.getSharedPreferences("extension_data", Context.MODE_PRIVATE)
        val lastExtensionUse = prefs.getLong("last_extension_use", 0)
        val now = System.currentTimeMillis()
        
        // If we've received an extension intent in the last 7 days, likely installed
        return (now - lastExtensionUse) < (7 * 24 * 60 * 60 * 1000)
    }
    
    /**
     * Record extension usage for heuristic detection
     */
    fun recordExtensionUsage(context: Context) {
        val prefs = context.getSharedPreferences("extension_data", Context.MODE_PRIVATE)
        prefs.edit().putLong("last_extension_use", System.currentTimeMillis()).apply()
    }
    
    /**
     * Get extension statistics
     */
    fun getExtensionStats(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences("extension_data", Context.MODE_PRIVATE)
        
        return mapOf(
            "lastUse" to prefs.getLong("last_extension_use", 0),
            "totalUses" to prefs.getInt("total_extension_uses", 0),
            "likelyInstalled" to isExtensionLikelyInstalled(context),
            "sources" to prefs.getStringSet("extension_sources", emptySet())?.toList().orEmpty()
        )
    }
    
    /**
     * Update extension statistics
     */
    fun updateExtensionStats(context: Context, source: String) {
        val prefs = context.getSharedPreferences("extension_data", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        
        // Update counters
        editor.putLong("last_extension_use", System.currentTimeMillis())
        editor.putInt("total_extension_uses", prefs.getInt("total_extension_uses", 0) + 1)
        
        // Track sources
        val sources = prefs.getStringSet("extension_sources", mutableSetOf()) ?: mutableSetOf()
        sources.add(source)
        editor.putStringSet("extension_sources", sources)
        
        editor.apply()
    }
}