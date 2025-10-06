package fe.linksheet.viewmodel

import androidx.lifecycle.ViewModel
import fe.linksheet.composable.ui.ThemeV2
import fe.linksheet.util.LinkSheetLinkTags
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class ThemeSettingsViewModel : ViewModel() {
    private val _themeMaterialYou = MutableStateFlow(false)
    val themeMaterialYou: StateFlow<Boolean> = _themeMaterialYou
    
    private val _themeV2: MutableStateFlow<ThemeV2> = MutableStateFlow(ThemeV2.System)
    val themeV2: StateFlow<ThemeV2> = _themeV2
    
    private val _themeAmoled = MutableStateFlow(false)
    val themeAmoled: StateFlow<Boolean> = _themeAmoled
    
    private val _linkAssets: MutableStateFlow<LinkSheetLinkTags> = MutableStateFlow(LinkSheetLinkTags(urlIds = emptyMap()))
    val linkAssets: StateFlow<LinkSheetLinkTags> = _linkAssets
    
    fun updateThemeMaterialYou(enabled: Boolean) {
        _themeMaterialYou.value = enabled
    }
    
    fun updateThemeV2(theme: ThemeV2) {
        _themeV2.value = theme
    }
    
    fun updateThemeAmoled(enabled: Boolean) {
        _themeAmoled.value = enabled
    }
    
    fun updateLinkAssets(linkTags: LinkSheetLinkTags) {
        _linkAssets.value = linkTags
    }
}
