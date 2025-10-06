package fe.linksheet.module.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Stub implementation for Play Store compliance
// History feature temporarily disabled
class HistoryViewModel : ViewModel() {
    private val _state = MutableStateFlow<HistoryState>(HistoryState.Empty)
    val state: StateFlow<HistoryState> = _state
}

sealed class HistoryState {
    object Empty : HistoryState()
    object Loading : HistoryState()
}
