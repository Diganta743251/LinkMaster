package fe.linksheet.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class NotificationSettingsViewModel : ViewModel() {
    private val _urlCopiedToast = MutableStateFlow(true)
    val urlCopiedToast: StateFlow<Boolean> = _urlCopiedToast
    
    private val _downloadStartedToast = MutableStateFlow(true)
    val downloadStartedToast: StateFlow<Boolean> = _downloadStartedToast
    
    private val _openingWithAppToast = MutableStateFlow(true)
    val openingWithAppToast: StateFlow<Boolean> = _openingWithAppToast
    
    private val _resolveViaToast = MutableStateFlow(true)
    val resolveViaToast: StateFlow<Boolean> = _resolveViaToast
    
    private val _resolveViaFailedToast = MutableStateFlow(true)
    val resolveViaFailedToast: StateFlow<Boolean> = _resolveViaFailedToast
    
    fun updateUrlCopiedToast(show: Boolean) {
        _urlCopiedToast.value = show
    }
    
    fun updateDownloadStartedToast(show: Boolean) {
        _downloadStartedToast.value = show
    }
    
    fun updateOpeningWithAppToast(show: Boolean) {
        _openingWithAppToast.value = show
    }
    
    fun updateResolveViaToast(show: Boolean) {
        _resolveViaToast.value = show
    }
    
    fun updateResolveViaFailedToast(show: Boolean) {
        _resolveViaFailedToast.value = show
    }
}
