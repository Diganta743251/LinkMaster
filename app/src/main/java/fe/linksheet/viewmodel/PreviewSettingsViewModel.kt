package fe.linksheet.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class PreviewSettingsViewModel : ViewModel() {
    private val _urlPreview = MutableStateFlow(true)
    val urlPreview: StateFlow<Boolean> = _urlPreview
    
    private val _urlPreviewSkipBrowser = MutableStateFlow(false)
    val urlPreviewSkipBrowser: StateFlow<Boolean> = _urlPreviewSkipBrowser
    
    fun updateUrlPreview(enabled: Boolean) {
        _urlPreview.value = enabled
    }
    
    fun updateUrlPreviewSkipBrowser(skip: Boolean) {
        _urlPreviewSkipBrowser.value = skip
    }
}
