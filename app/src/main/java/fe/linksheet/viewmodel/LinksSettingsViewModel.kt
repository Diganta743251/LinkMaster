package fe.linksheet.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class LinksSettingsViewModel : ViewModel() {
    private val _useClearUrls = MutableStateFlow(true)
    val useClearUrls: StateFlow<Boolean> = _useClearUrls
    
    private val _useFastForwardRules = MutableStateFlow(true)
    val useFastForwardRules: StateFlow<Boolean> = _useFastForwardRules
    
    private val _enableLibRedirect = MutableStateFlow(false)
    val enableLibRedirect: StateFlow<Boolean> = _enableLibRedirect
    
    private val _followRedirects = MutableStateFlow(true)
    val followRedirects: StateFlow<Boolean> = _followRedirects
    
    private val _enableAmp2Html = MutableStateFlow(false)
    val enableAmp2Html: StateFlow<Boolean> = _enableAmp2Html
    
    private val _enableDownloader = MutableStateFlow(false)
    val enableDownloader: StateFlow<Boolean> = _enableDownloader
    
    private val _urlPreview = MutableStateFlow(true)
    val urlPreview: StateFlow<Boolean> = _urlPreview
    
    private val _resolveEmbeds = MutableStateFlow(false)
    val resolveEmbeds: StateFlow<Boolean> = _resolveEmbeds
    
    fun updateUseClearUrls(enabled: Boolean) {
        _useClearUrls.value = enabled
    }
    
    fun updateUseFastForwardRules(enabled: Boolean) {
        _useFastForwardRules.value = enabled
    }
    
    fun updateEnableLibRedirect(enabled: Boolean) {
        _enableLibRedirect.value = enabled
    }
    
    fun updateFollowRedirects(enabled: Boolean) {
        _followRedirects.value = enabled
    }
    
    fun updateEnableAmp2Html(enabled: Boolean) {
        _enableAmp2Html.value = enabled
    }
    
    fun updateEnableDownloader(enabled: Boolean) {
        _enableDownloader.value = enabled
    }
    
    fun updateUrlPreview(enabled: Boolean) {
        _urlPreview.value = enabled
    }
    
    fun updateResolveEmbeds(enabled: Boolean) {
        _resolveEmbeds.value = enabled
    }
}
