package fe.linksheet.activity.bottomsheet.content.pending

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fe.linksheet.R
import fe.linksheet.module.resolver.ResolveEvent
import fe.linksheet.module.resolver.ResolverInteraction

@Composable
fun M3ELoadingIndicatorSheetContent(
    modifier: Modifier = Modifier,
    event: ResolveEvent,
    interaction: ResolverInteraction,
    requestExpand: () -> Unit,
) {
    LaunchedEffect(key1 = interaction) {
        Log.d(
            "LoadingIndicatorSheetContent",
            "Interaction=$interaction, isClear=${interaction == ResolverInteraction.Clear}, " +
                    "isInitialized=${interaction == ResolverInteraction.Initialized}"
        )
        if (interaction != ResolverInteraction.Initialized) {
            // Request resize on interaction change to accommodate interaction UI
            requestExpand()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.loading_link),
                style = MaterialTheme.typography.titleMedium
            )

            Text(text = event.name, style = MaterialTheme.typography.bodyMedium)
        }

        if (LocalInspectionMode.current) {
            CircularProgressIndicator(progress = { 0.7f })
        } else {
            CircularProgressIndicator()
        }

        if (interaction is ResolverInteraction.Cancelable) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    contentPadding =
                        PaddingValues(
                            start = 62.dp,
                            top = 12.dp,
                            end = 62.dp,
                            bottom = 12.dp
                        ),
                    onClick = {
                        Log.d("Interact", "Cancel")
                        interaction.cancel()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.bottom_sheet_loading_indicator__button_skip_job),
                    )
                }

            }
        }
//        LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp))
    }
}
