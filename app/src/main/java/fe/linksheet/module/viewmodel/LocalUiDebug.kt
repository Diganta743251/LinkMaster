package fe.linksheet.module.viewmodel

import androidx.compose.runtime.compositionLocalOf
import fe.linksheet.module.debug.DebugPreferenceProvider

val LocalUiDebug = compositionLocalOf<DebugPreferenceProvider> { 
    object : DebugPreferenceProvider {
        // Stub implementation
    }
}
