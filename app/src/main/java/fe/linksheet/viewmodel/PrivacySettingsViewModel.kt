package fe.linksheet.viewmodel

import androidx.lifecycle.ViewModel
import fe.linksheet.data.LinkHistoryDuration
import fe.linksheet.data.LinkHistoryLimit
import fe.linksheet.R
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class PrivacySettingsViewModel : ViewModel() {
    private val _linkHistoryDuration = MutableStateFlow(LinkHistoryDuration.THIRTY_DAYS)
    val linkHistoryDuration: StateFlow<LinkHistoryDuration> = _linkHistoryDuration
    
    private val _linkHistoryLimit = MutableStateFlow(LinkHistoryLimit.ONE_THOUSAND)
    val linkHistoryLimit: StateFlow<LinkHistoryLimit> = _linkHistoryLimit
    
    private val _enableLinkHistory = MutableStateFlow(true)
    val enableLinkHistory: StateFlow<Boolean> = _enableLinkHistory
    
    private val _showAsReferrer = MutableStateFlow(false)
    val showAsReferrer: StateFlow<Boolean> = _showAsReferrer
    
    private val _showLinkHistoryDurationDialog = MutableStateFlow(false)
    val showLinkHistoryDurationDialog: StateFlow<Boolean> = _showLinkHistoryDurationDialog
    
    private val _showLinkHistoryLimitDialog = MutableStateFlow(false)
    val showLinkHistoryLimitDialog: StateFlow<Boolean> = _showLinkHistoryLimitDialog
    
    private val _showClearHistoryDialog = MutableStateFlow(false)
    val showClearHistoryDialog: StateFlow<Boolean> = _showClearHistoryDialog
    
    private val _telemetryLevel = MutableStateFlow(TelemetryLevel.BASIC)
    val telemetryLevel: StateFlow<TelemetryLevel> = _telemetryLevel
    
    private val _enableAnalytics = MutableStateFlow(true)
    val enableAnalytics: StateFlow<Boolean> = _enableAnalytics
    
    fun updateLinkHistoryDuration(duration: LinkHistoryDuration) {
        _linkHistoryDuration.value = duration
    }
    
    fun updateLinkHistoryLimit(limit: LinkHistoryLimit) {
        _linkHistoryLimit.value = limit
    }
    
    fun showLinkHistoryDurationDialog() {
        _showLinkHistoryDurationDialog.value = true
    }
    
    fun hideLinkHistoryDurationDialog() {
        _showLinkHistoryDurationDialog.value = false
    }
    
    fun showLinkHistoryLimitDialog() {
        _showLinkHistoryLimitDialog.value = true
    }
    
    fun hideLinkHistoryLimitDialog() {
        _showLinkHistoryLimitDialog.value = false
    }
    
    fun showClearHistoryDialog() {
        _showClearHistoryDialog.value = true
    }
    
    fun hideClearHistoryDialog() {
        _showClearHistoryDialog.value = false
    }
    
    fun clearLinkHistory() {
        // Implementation for clearing link history
        hideClearHistoryDialog()
    }
    
    fun getLinkHistoryDurationText(): String {
        return when (val duration = _linkHistoryDuration.value) {
            LinkHistoryDuration.DISABLED -> "Disabled"
            LinkHistoryDuration.ONE_DAY -> "1 day"
            LinkHistoryDuration.SEVEN_DAYS -> "7 days"
            LinkHistoryDuration.FOURTEEN_DAYS -> "14 days"
            LinkHistoryDuration.THIRTY_DAYS -> "30 days"
            is LinkHistoryDuration.CUSTOM_DAYS -> "${duration.days} days"
        }
    }
    
    fun getLinkHistoryLimitText(): String {
        return when (val limit = _linkHistoryLimit.value) {
            LinkHistoryLimit.NO_LIMIT -> "No limit"
            LinkHistoryLimit.ONE_HUNDRED -> "100"
            LinkHistoryLimit.FIVE_HUNDRED -> "500"
            LinkHistoryLimit.ONE_THOUSAND -> "1,000"
            LinkHistoryLimit.FIVE_THOUSAND -> "5,000"
            LinkHistoryLimit.TEN_THOUSAND -> "10,000"
            is LinkHistoryLimit.CUSTOM_COUNT -> "${limit.count}"
        }
    }
    
    fun resetIdentifier() {
        // Implementation for resetting identifier
    }
}

enum class TelemetryLevel(val titleId: Int) {
    BASIC(R.string.telemetry_basic),
    ENHANCED(R.string.telemetry_enhanced),
    FULL(R.string.telemetry_full)
}
