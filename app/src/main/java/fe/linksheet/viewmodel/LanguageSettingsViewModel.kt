package fe.linksheet.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class LanguageSettingsViewModel : ViewModel() {
    private val _deviceLocaleFlow = MutableStateFlow<String>("en")
    val deviceLocaleFlow: StateFlow<String> = _deviceLocaleFlow
    
    private val _appLocaleItemFlow = MutableStateFlow<String>("en")
    val appLocaleItemFlow: StateFlow<String> = _appLocaleItemFlow
    
    private val _localesFlow = MutableStateFlow<List<String>>(listOf("en", "es", "fr", "de"))
    val localesFlow: StateFlow<List<String>> = _localesFlow
    
    fun updateDeviceLocale(locale: String) {
        _deviceLocaleFlow.value = locale
    }
    
    fun updateAppLocale(locale: String) {
        _appLocaleItemFlow.value = locale
    }
    
    fun updateLocales(locales: List<String>) {
        _localesFlow.value = locales
    }
}
