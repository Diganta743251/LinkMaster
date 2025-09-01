package fe.linksheet.composable.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

fun getColorScheme(isDark: Boolean): ColorScheme {
    return if (isDark) darkColorScheme() else lightColorScheme()
}
