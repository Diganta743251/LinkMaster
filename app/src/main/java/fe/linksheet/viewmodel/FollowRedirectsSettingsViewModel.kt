package fe.linksheet.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class FollowRedirectsSettingsViewModel : ViewModel() {
    private val _followRedirects = MutableStateFlow(true)
    val followRedirects: StateFlow<Boolean> = _followRedirects
    
    private val _followRedirectsLocalCache = MutableStateFlow(true)
    val followRedirectsLocalCache: StateFlow<Boolean> = _followRedirectsLocalCache
    
    private val _followRedirectsExternalService = MutableStateFlow(false)
    val followRedirectsExternalService: StateFlow<Boolean> = _followRedirectsExternalService
    
    private val _followOnlyKnownTrackers = MutableStateFlow(false)
    val followOnlyKnownTrackers: StateFlow<Boolean> = _followOnlyKnownTrackers
    
    private val _followRedirectsAllowsDarknets = MutableStateFlow(false)
    val followRedirectsAllowsDarknets: StateFlow<Boolean> = _followRedirectsAllowsDarknets
    
    private val _followRedirectsAllowLocalNetwork = MutableStateFlow(false)
    val followRedirectsAllowLocalNetwork: StateFlow<Boolean> = _followRedirectsAllowLocalNetwork
    
    private val _followRedirectsSkipBrowser = MutableStateFlow(false)
    val followRedirectsSkipBrowser: StateFlow<Boolean> = _followRedirectsSkipBrowser
    
    private val _requestTimeout = MutableStateFlow(5000)
    val requestTimeout: StateFlow<Int> = _requestTimeout
    
    fun updateFollowRedirects(enabled: Boolean) {
        _followRedirects.value = enabled
    }
    
    fun updateFollowRedirectsLocalCache(enabled: Boolean) {
        _followRedirectsLocalCache.value = enabled
    }
    
    fun updateFollowRedirectsExternalService(enabled: Boolean) {
        _followRedirectsExternalService.value = enabled
    }
    
    fun updateFollowOnlyKnownTrackers(enabled: Boolean) {
        _followOnlyKnownTrackers.value = enabled
    }
    
    fun updateFollowRedirectsAllowsDarknets(allows: Boolean) {
        _followRedirectsAllowsDarknets.value = allows
    }
    
    fun updateFollowRedirectsAllowLocalNetwork(allows: Boolean) {
        _followRedirectsAllowLocalNetwork.value = allows
    }
    
    fun updateFollowRedirectsSkipBrowser(skip: Boolean) {
        _followRedirectsSkipBrowser.value = skip
    }
    
    fun updateRequestTimeout(timeout: Int) {
        _requestTimeout.value = timeout
    }
}
