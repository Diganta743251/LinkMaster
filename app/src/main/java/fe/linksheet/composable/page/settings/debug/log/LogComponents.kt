package fe.linksheet.composable.page.settings.debug.log

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Minimal stub used by CrashHandlerActivity and ExportLogDialog

data class PrefixMessageCardContent(
    val type: String,
    val prefix: String,
    val start: Long,
    val messages: MutableList<String>
)

@Composable
fun LogCard(
    border: BorderStroke = BorderStroke(0.dp, androidx.compose.ui.graphics.Color.Unspecified),
    logEntry: PrefixMessageCardContent,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Card(modifier = modifier, border = border) {
        Text(text = "${logEntry.prefix}: ${logEntry.messages.joinToString()}" )
    }
}
