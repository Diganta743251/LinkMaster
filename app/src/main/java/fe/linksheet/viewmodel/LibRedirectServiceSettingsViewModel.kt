package fe.linksheet.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class LibRedirectServiceSettingsViewModel : ViewModel() {
    private val _settings = MutableStateFlow<Any?>(null)
    val settings: StateFlow<Any?> = _settings
    
    private val _selectedFrontend = MutableStateFlow<String?>(null)
    val selectedFrontend: StateFlow<String?> = _selectedFrontend
    
    fun updateSettings(settings: Any?) {
        _settings.value = settings
    }
    
    fun updateSelectedFrontend(frontend: String?) {
        _selectedFrontend.value = frontend
    }
}
