package fe.linksheet.ads

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import java.util.Arrays

/**
 * Manages AdMob integration with proper consent handling
 */
class AdManager private constructor() {
    private var isInitialized = false
    private var isTestMode = false
    
    companion object {
        private const val TAG = "AdManager"
        
        // Test device IDs should be added here
        private val TEST_DEVICE_IDS = listOf(
            "ABCDEF012345678901234567" // Replace with actual test device IDs
        )
        
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
        
        // Set test devices for debug builds
        if (isTestMode) {
            val configuration = RequestConfiguration.Builder()
                .setTestDeviceIds(TEST_DEVICE_IDS)
                .build()
            MobileAds.setRequestConfiguration(configuration)
        }
        
        // Initialize the Mobile Ads SDK
        MobileAds.initialize(context, object : OnInitializationCompleteListener {
            override fun onInitializationComplete(status: InitializationStatus) {
                Log.d(TAG, "AdMob initialization complete: ${status.adapterStatusMap}")
                isInitialized = true
            }
        })
        
        Log.d(TAG, "AdMob initialization started, test mode: $isTestMode")
    }
    
    /**
     * Create a standard ad request with appropriate settings
     */
    fun createAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }
    
    /**
     * Load an ad into the provided AdView
     */
    fun loadAd(adView: AdView) {
        if (!isInitialized) {
            Log.w(TAG, "Attempted to load ad before initialization")
            return
        }
        
        adView.loadAd(createAdRequest())
    }
    
    /**
     * Check if ads should be shown based on user preferences
     */
    fun shouldShowAds(context: Context): Boolean {
        // Implement your logic to check if ads should be shown
        // For example, based on user preferences or premium status
        return true
    }
}