package fe.linksheet.composable.page.settings.apps.verifiedlinkhandlers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fe.android.compose.content.rememberOptionalContent
import fe.linksheet.R

data class FilterState(
    val mode: VlhStateModeFilter,
    val type: VlhTypeFilter,
    val systemApps: Boolean
)

@Composable
internal fun FilterColumn(
    state: FilterState,
    onChange: (FilterState) -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.generic__title_filter),
            style = MaterialTheme.typography.titleMedium
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
//                    .padding(horizontal = SaneLazyColumnDefaults.HorizontalSpacing),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            VlhStateModeFilter(
                selection = state.mode,
                onSelected = { onChange(state.copy(mode = it)) }
            )

            VlhTypeFilter(
                selection = state.type,
                onSelected = { onChange(state.copy(type = it)) }
            )

            FilterChip(
                selected = state.systemApps,
                onClick = { onChange(state.copy(systemApps = !state.systemApps)) },
                label = {
                    Text(text = stringResource(id = R.string.settings_verified_link_handlers__text_system_apps))
                },
                leadingIcon = rememberOptionalContent(state.systemApps) {
                    Icon(
                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                        imageVector = Icons.Filled.Done,
                        contentDescription = null,
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterColumnPreview() {
    FilterColumn(
        state = FilterState(VlhStateModeFilter.ShowAll, VlhTypeFilter.All, true),
        onChange = {

        },
    )
}
