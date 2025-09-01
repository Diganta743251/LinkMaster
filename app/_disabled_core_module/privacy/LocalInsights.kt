package fe.linksheet.module.privacy

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Privacy-focused local insights
 * Provides usage statistics without any data collection or transmission
 * All data stays on device and can be cleared by user
 */
class LocalInsights(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("local_insights", Context.MODE_PRIVATE)
    
    private val _insights = MutableStateFlow(getInsights())
    val insights: StateFlow<LocalInsightsData> = _insights.asStateFlow()
    
    /**
     * Record app launch (local only)
     */
    fun recordAppLaunch() {
        val count = prefs.getInt(KEY_APP_LAUNCHES, 0) + 1
        prefs.edit().putInt(KEY_APP_LAUNCHES, count).apply()
        updateLastUsed()
        refreshInsights()
    }
    
    /**
     * Record link processed (local only)
     */
    fun recordLinkProcessed() {
        val count = prefs.getInt(KEY_LINKS_PROCESSED, 0) + 1
        prefs.edit().putInt(KEY_LINKS_PROCESSED, count).apply()
        updateLastUsed()
        refreshInsights()
    }
    
    /**
     * Record QR code generated (local only)
     */
    fun recordQRGenerated() {
        val count = prefs.getInt(KEY_QR_GENERATED, 0) + 1
        prefs.edit().putInt(KEY_QR_GENERATED, count).apply()
        updateLastUsed()
        refreshInsights()
    }
    
    /**
     * Record theme change (local only)
     */
    fun recordThemeChange() {
        val count = prefs.getInt(KEY_THEME_CHANGES, 0) + 1
        prefs.edit().putInt(KEY_THEME_CHANGES, count).apply()
        updateLastUsed()
        refreshInsights()
    }
    
    /**
     * Record widget interaction (local only)
     */
    fun recordWidgetInteraction() {
        val count = prefs.getInt(KEY_WIDGET_INTERACTIONS, 0) + 1
        prefs.edit().putInt(KEY_WIDGET_INTERACTIONS, count).apply()
        updateLastUsed()
        refreshInsights()
    }
    
    /**
     * Clear all local insights data
     */
    fun clearAllData() {
        prefs.edit().clear().apply()
        refreshInsights()
    }
    
    /**
     * Get current insights data
     */
    private fun getInsights(): LocalInsightsData {
        return LocalInsightsData(
            appLaunches = prefs.getInt(KEY_APP_LAUNCHES, 0),
            linksProcessed = prefs.getInt(KEY_LINKS_PROCESSED, 0),
            qrCodesGenerated = prefs.getInt(KEY_QR_GENERATED, 0),
            themeChanges = prefs.getInt(KEY_THEME_CHANGES, 0),
            widgetInteractions = prefs.getInt(KEY_WIDGET_INTERACTIONS, 0),
            firstUsed = prefs.getString(KEY_FIRST_USED, null),
            lastUsed = prefs.getString(KEY_LAST_USED, null)
        )
    }
    
    private fun updateLastUsed() {
        val now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        prefs.edit().putString(KEY_LAST_USED, now).apply()
        
        // Set first used if not already set
        if (!prefs.contains(KEY_FIRST_USED)) {
            prefs.edit().putString(KEY_FIRST_USED, now).apply()
        }
    }
    
    private fun refreshInsights() {
        _insights.value = getInsights()
    }
    
    companion object {
        private const val KEY_APP_LAUNCHES = "app_launches"
        private const val KEY_LINKS_PROCESSED = "links_processed"
        private const val KEY_QR_GENERATED = "qr_generated"
        private const val KEY_THEME_CHANGES = "theme_changes"
        private const val KEY_WIDGET_INTERACTIONS = "widget_interactions"
        private const val KEY_FIRST_USED = "first_used"
        private const val KEY_LAST_USED = "last_used"
    }
}

/**
 * Local insights data structure
 */
data class LocalInsightsData(
    val appLaunches: Int = 0,
    val linksProcessed: Int = 0,
    val qrCodesGenerated: Int = 0,
    val themeChanges: Int = 0,
    val widgetInteractions: Int = 0,
    val firstUsed: String? = null,
    val lastUsed: String? = null
) {
    /**
     * Get total interactions
     */
    val totalInteractions: Int
        get() = appLaunches + linksProcessed + qrCodesGenerated + themeChanges + widgetInteractions
    
    /**
     * Check if user is active
     */
    val isActiveUser: Boolean
        get() = totalInteractions > 10
    
    /**
     * Get usage summary
     */
    val usageSummary: String
        get() = when {
            totalInteractions == 0 -> "New user"
            totalInteractions < 10 -> "Getting started"
            totalInteractions < 50 -> "Regular user"
            totalInteractions < 200 -> "Active user"
            else -> "Power user"
        }
}