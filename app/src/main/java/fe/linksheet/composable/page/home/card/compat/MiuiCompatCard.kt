package fe.linksheet.composable.page.home.card.compat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fe.android.compose.icon.iconPainter
import fe.android.compose.text.StringResourceContent.Companion.textContent
import fe.composekit.component.ContentType
import fe.composekit.component.card.AlertCard
import fe.composekit.route.Route
import fe.linksheet.R
import fe.linksheet.composable.ui.PreviewTheme
import fe.linksheet.navigation.MarkdownViewerRoute
import fe.linksheet.util.LinkSheet

fun LazyListScope.MiuiCompatCardWrapper(navigate: (Route) -> Unit, onClick: () -> Unit) {
    item(
        key = R.string.settings_main_miui_compat__title_linksheet_auto_start_failure,
        contentType = ContentType.ClickableAlert
    ) {
        MiuiCompatCard(navigate = navigate, onClick = onClick)
    }
}

@Composable
fun MiuiCompatCard(navigate: (Route) -> Unit, onClick: () -> Unit) {
    AlertCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        icon = Icons.Rounded.ErrorOutline.iconPainter,
        iconContentDescription = null,
        headline = textContent(id = R.string.settings_main_miui_compat__title_linksheet_auto_start_failure),
        subtitle = textContent(id = R.string.settings_main_miui_compat__text_linksheet_auto_start_info),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                onClick = onClick
            ) {
                Text(text = stringResource(id = R.string.settings_main_miui_compat__button_linksheet_auto_start_allow))
            }

            TextButton(onClick = {
                navigate(MarkdownViewerRoute(LinkSheet.WikiDeviceIssuesXiaomi2))
            }) {
                Text(text = stringResource(id = R.string.generic__button_learn_more))
            }
        }
    }
}

@Preview
@Composable
private fun MiuiCompatCardPreview() {
    PreviewTheme {
    }
}
