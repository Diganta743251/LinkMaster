package fe.linksheet.composable.ui

import androidx.compose.runtime.compositionLocalOf
import fe.linksheet.module.debug.DebugPreferenceProvider

val LocalUiDebug = compositionLocalOf<DebugPreferenceProvider> { 
    error("LocalUiDebug not provided") 
}
