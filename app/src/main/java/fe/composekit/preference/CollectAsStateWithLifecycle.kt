package fe.composekit.preference

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <T> StateFlow<T>.collectAsStateWithLifecycle(): State<T> {
    // Stub implementation - return current value
    return androidx.compose.runtime.mutableStateOf(this.value)
}
