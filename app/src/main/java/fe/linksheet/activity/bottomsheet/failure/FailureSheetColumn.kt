package fe.linksheet.activity.bottomsheet.failure

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fe.linksheet.R

@Composable
fun FailureSheetColumn(
    onShareClick: () -> Unit,
    onCopyClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 10.dp, horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.no_handlers_found),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = stringResource(id = R.string.no_handlers_found_explainer),
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start)) {
            Button(onClick = onCopyClick) {
                Text(text = stringResource(id = R.string.copy_url))
            }

            Button(onClick = onShareClick) {
                Text(text = stringResource(id = R.string.send_to))
            }
        }
    }
}

