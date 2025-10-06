package fe.linksheet.activity.bottomsheet.content.failure

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fe.linksheet.util.intent.parser.UriException

@Composable
fun FailureSheetContentWrapper(
    modifier: Modifier = Modifier,
    exception: UriException,
    onShareClick: () -> Unit,
    onCopyClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Unable to parse link",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = exception::class.simpleName ?: "Unknown error",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
