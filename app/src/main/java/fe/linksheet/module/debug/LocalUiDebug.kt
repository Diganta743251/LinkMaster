package fe.linksheet.module.debug

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow

val LocalUiDebug = compositionLocalOf<DebugPreferenceProvider> {
    object : DebugPreferenceProvider {
        override val drawBorders = MutableStateFlow(false)
        override val bottomSheetLog = MutableStateFlow(false)
    }
}
