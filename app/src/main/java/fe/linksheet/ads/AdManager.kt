package fe.linksheet.ads

import android.content.Context
import android.util.Log

/**
 * Manages AdMob integration with proper consent handling
 */
class AdManager private constructor() {
    private var isInitialized = false
    private var isTestMode = false
    
    companion object {
        private const val TAG = "AdManager"
        
        @Volatile
        private var instance: AdManager? = null
        
        fun getInstance(): AdManager {
            return instance ?: synchronized(this) {
                instance ?: AdManager().also { instance = it }
            }
        }
    }
    
    /**
     * Initialize AdMob with proper configuration
     * Should be called from Application class
     */
    fun initialize(context: Context, isDebugBuild: Boolean) {
        if (isInitialized) return
        
        isTestMode = isDebugBuild
        // Stubbed: No-op to avoid Play Services dependency during core build
        isInitialized = true
        Log.d(TAG, "Ad subsystem initialized (stub), test mode: $isTestMode")
    }
    
    /**
     * Create a standard ad request with appropriate settings
     */
    fun createAdRequest(): Any? = null
    
    /**
     * Load an ad into the provided AdView
     */
    fun loadAd(@Suppress("UNUSED_PARAMETER") adView: Any) { /* no-op */ }
    
    /**
     * Check if ads should be shown based on user preferences
     */
    fun shouldShowAds(context: Context): Boolean {
        // Implement your logic to check if ads should be shown
        // For example, based on user preferences or premium status
        return true
    }
}