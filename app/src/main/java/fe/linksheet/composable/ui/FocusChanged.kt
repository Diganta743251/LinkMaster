package fe.linksheet.composable.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged

@Composable
fun Modifier.onFocusChanged(
    onFocusChanged: (Boolean) -> Unit
): Modifier = this.onFocusChanged { focusState ->
    onFocusChanged(focusState.isFocused)
}

@Composable
fun rememberFocusRequester(): FocusRequester = remember { FocusRequester() }
