package fe.linksheet.ads

import android.content.Context
import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Manages user consent for personalized ads
 * This is a simplified implementation - in a real app, you would use
 * the User Messaging Platform (UMP) SDK for GDPR compliance
 */
class ConsentManager private constructor() {
    private val consentGiven = AtomicBoolean(false)
    
    companion object {
        private const val TAG = "ConsentManager"
        private const val PREF_NAME = "ad_consent_prefs"
        private const val KEY_CONSENT_GIVEN = "consent_given"
        
        @Volatile
        private var instance: ConsentManager? = null
        
        fun getInstance(): ConsentManager {
            return instance ?: synchronized(this) {
                instance ?: ConsentManager().also { instance = it }
            }
        }
    }
    
    /**
     * Initialize consent manager and load saved preferences
     */
    fun initialize(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedConsent = prefs.getBoolean(KEY_CONSENT_GIVEN, false)
        consentGiven.set(savedConsent)
        
        Log.d(TAG, "Consent manager initialized, consent status: $savedConsent")
    }
    
    /**
     * Check if user has given consent
     */
    fun hasConsent(): Boolean {
        return consentGiven.get()
    }
    
    /**
     * Update consent status and save to preferences
     */
    fun updateConsent(context: Context, consent: Boolean) {
        consentGiven.set(consent)
        
        // Save to preferences
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_CONSENT_GIVEN, consent)
            .apply()
        
        Log.d(TAG, "Consent updated: $consent")
    }
    
    /**
     * Reset consent status
     */
    fun resetConsent(context: Context) {
        updateConsent(context, false)
    }
}