package fe.linksheet.module.viewmodel


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import fe.linksheet.module.analytics.BaseAnalyticsService
import fe.linksheet.module.analytics.TelemetryLevel
import fe.linksheet.module.preference.SensitivePreference
import fe.linksheet.module.preference.app.AppPreferenceRepository
import fe.linksheet.module.preference.app.AppPreferences
// Experiment imports removed - violates Play Store policies
import fe.linksheet.module.viewmodel.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PrivacySettingsViewModel(
    val context: Application,
    preferenceRepository: AppPreferenceRepository,
    // experimentsRepository: ExperimentRepository, // Removed - violates Play Store policies
    private val analyticsService: BaseAnalyticsService,
) : BaseViewModel(preferenceRepository) {
    val showAsReferrer = preferenceRepository.asViewModelState(AppPreferences.showLinkSheetAsReferrer)
    // Analytics removed - violates Play Store policies

    @OptIn(SensitivePreference::class)
    val telemetryLevel = preferenceRepository.asViewModelState(AppPreferences.telemetryLevel)

    // Link History Settings
    val enableLinkHistory = preferenceRepository.asViewModelState(AppPreferences.enableLinkHistory)
    
    private val _linkHistoryDuration = MutableStateFlow<LinkHistoryDuration>(LinkHistoryDuration.DISABLED)
    val linkHistoryDuration = _linkHistoryDuration.asStateFlow()
    
    private val _linkHistoryLimit = MutableStateFlow<LinkHistoryLimit>(LinkHistoryLimit.DISABLED)
    val linkHistoryLimit = _linkHistoryLimit.asStateFlow()
    
    var showLinkHistoryDurationDialog by mutableStateOf(false)
        private set
    
    var showLinkHistoryLimitDialog by mutableStateOf(false)
        private set
    
    var showClearHistoryDialog by mutableStateOf(false)
        private set

    fun updateTelemetryLevel(level: TelemetryLevel) {
        telemetryLevel(level)
        // TODO: Cancel old job?
        analyticsService.changeLevel(level)
    }

    fun resetIdentifier() {

    }
    
    // Link History Management Functions
    fun showLinkHistoryDurationDialog() {
        showLinkHistoryDurationDialog = true
    }
    
    fun hideLinkHistoryDurationDialog() {
        showLinkHistoryDurationDialog = false
    }
    
    fun showLinkHistoryLimitDialog() {
        showLinkHistoryLimitDialog = true
    }
    
    fun hideLinkHistoryLimitDialog() {
        showLinkHistoryLimitDialog = false
    }
    
    fun showClearHistoryDialog() {
        showClearHistoryDialog = true
    }
    
    fun hideClearHistoryDialog() {
        showClearHistoryDialog = false
    }
    
    fun updateLinkHistoryDuration(duration: LinkHistoryDuration) {
        _linkHistoryDuration.value = duration
        // TODO: Save to preferences and trigger cleanup
        hideLinkHistoryDurationDialog()
    }
    
    fun updateLinkHistoryLimit(limit: LinkHistoryLimit) {
        _linkHistoryLimit.value = limit
        // TODO: Save to preferences and trigger cleanup
        hideLinkHistoryLimitDialog()
    }
    
    fun clearLinkHistory() {
        // TODO: Clear all link history
        hideClearHistoryDialog()
    }
    
    fun getLinkHistoryDurationText(): String {
        return when (val value = _linkHistoryDuration.value) {
            LinkHistoryDuration.DISABLED -> "Disabled"
            LinkHistoryDuration.ONE_DAY -> "1 day"
            LinkHistoryDuration.SEVEN_DAYS -> "7 days"
            LinkHistoryDuration.FOURTEEN_DAYS -> "14 days"
            LinkHistoryDuration.THIRTY_DAYS -> "30 days"
            is LinkHistoryDuration.CUSTOM_DAYS -> "${value.days} days"
            else -> "Unknown"
        }
    }
    
    fun getLinkHistoryLimitText(): String {
        return when (val value = _linkHistoryLimit.value) {
            LinkHistoryLimit.DISABLED -> "No limit"
            LinkHistoryLimit.ONE_HUNDRED -> "Last 100 links"
            LinkHistoryLimit.ONE_THOUSAND -> "Last 1000 links"
            is LinkHistoryLimit.CUSTOM_COUNT -> "Last ${value.count} links"
            else -> "Unknown"
        }
    }
}

// Data classes for link history settings
sealed class LinkHistoryDuration {
    object DISABLED : LinkHistoryDuration()
    object ONE_DAY : LinkHistoryDuration()
    object SEVEN_DAYS : LinkHistoryDuration()
    object FOURTEEN_DAYS : LinkHistoryDuration()
    object THIRTY_DAYS : LinkHistoryDuration()
    data class CUSTOM_DAYS(val days: Int) : LinkHistoryDuration()
}

sealed class LinkHistoryLimit {
    object DISABLED : LinkHistoryLimit()
    object ONE_HUNDRED : LinkHistoryLimit()
    object ONE_THOUSAND : LinkHistoryLimit()
    data class CUSTOM_COUNT(val count: Int) : LinkHistoryLimit()
}
