package fe.linksheet.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class GeneralSettingsViewModel : ViewModel() {
    private val _alwaysShowPackageName = MutableStateFlow(false)
    val alwaysShowPackageName: StateFlow<Boolean> = _alwaysShowPackageName
    
    private val _homeClipboardCard = MutableStateFlow(true)
    val homeClipboardCard: StateFlow<Boolean> = _homeClipboardCard
    
    fun updateAlwaysShowPackageName(show: Boolean) {
        _alwaysShowPackageName.value = show
    }
    
    fun updateHomeClipboardCard(show: Boolean) {
        _homeClipboardCard.value = show
    }
}
