package fe.linksheet.module.viewmodel

import androidx.compose.runtime.compositionLocalOf
import fe.linksheet.module.debug.DebugPreferenceProvider
import kotlinx.coroutines.flow.MutableStateFlow

val LocalUiDebug = compositionLocalOf<DebugPreferenceProvider> { 
    object : DebugPreferenceProvider {
        override val drawBorders = MutableStateFlow(false)
        override val bottomSheetLog = MutableStateFlow(false)
    }
}
