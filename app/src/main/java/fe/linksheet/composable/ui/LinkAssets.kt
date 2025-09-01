package fe.linksheet.composable.ui

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

val linkAssets: StateFlow<Boolean> = MutableStateFlow(false)
