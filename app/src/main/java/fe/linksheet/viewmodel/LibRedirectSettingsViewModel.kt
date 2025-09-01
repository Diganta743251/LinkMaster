package fe.linksheet.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class LibRedirectSettingsViewModel : ViewModel() {
    private val _services = MutableStateFlow<List<String>>(emptyList())
    val services: StateFlow<List<String>> = _services
    
    fun updateServices(services: List<String>) {
        _services.value = services
    }
}
