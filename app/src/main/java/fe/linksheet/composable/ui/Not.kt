package fe.linksheet.composable.ui

import androidx.compose.runtime.State

operator fun State<Boolean>.not(): Boolean = !value
