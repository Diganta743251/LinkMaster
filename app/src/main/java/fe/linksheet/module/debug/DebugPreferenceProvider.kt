package fe.linksheet.module.debug

import kotlinx.coroutines.flow.StateFlow

interface DebugPreferenceProvider {
    val drawBorders: StateFlow<Boolean>
    val bottomSheetLog: StateFlow<Boolean>
}
