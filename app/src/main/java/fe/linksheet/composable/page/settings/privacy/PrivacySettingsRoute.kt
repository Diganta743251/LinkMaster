package fe.linksheet.composable.page.settings.privacy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import fe.android.compose.text.StringResourceContent.Companion.textContent
import fe.composekit.component.list.column.shape.ClickableShapeListItem
import fe.composekit.preference.collectAsStateWithLifecycle
import fe.linksheet.R
import fe.linksheet.composable.component.list.item.type.PreferenceSwitchListItem
import fe.linksheet.composable.component.page.SaneScaffoldSettingsPage
// Analytics dialog import removed - violates Play Store policies
import fe.linksheet.viewmodel.PrivacySettingsViewModel
import fe.linksheet.util.buildconfig.Build
import org.koin.androidx.compose.koinViewModel


@Composable
fun PrivacySettingsRoute(
    onBackPressed: () -> Unit,
    navigate: ((String) -> Unit)? = null,
    viewModel: PrivacySettingsViewModel = koinViewModel(),
) {
    // Analytics functionality removed - violates Play Store policies
    val linkHistoryDuration by viewModel.linkHistoryDuration.collectAsStateWithLifecycle()
    val linkHistoryLimit by viewModel.linkHistoryLimit.collectAsStateWithLifecycle()
    val showLinkHistoryDurationDialog by viewModel.showLinkHistoryDurationDialog.collectAsStateWithLifecycle()
    val showLinkHistoryLimitDialog by viewModel.showLinkHistoryLimitDialog.collectAsStateWithLifecycle()
    val showClearHistoryDialog by viewModel.showClearHistoryDialog.collectAsStateWithLifecycle()
    val showAsReferrer by viewModel.showAsReferrer.collectAsStateWithLifecycle()
    val enableLinkHistory by viewModel.enableLinkHistory.collectAsStateWithLifecycle()
    val telemetryLevel by viewModel.telemetryLevel.collectAsStateWithLifecycle()

    // Link History Dialogs
    if (showLinkHistoryDurationDialog) {
        LinkHistoryDurationDialog(
            currentDuration = linkHistoryDuration,
            onDurationSelected = viewModel::updateLinkHistoryDuration,
            onDismiss = viewModel::hideLinkHistoryDurationDialog
        )
    }

    if (showLinkHistoryLimitDialog) {
        LinkHistoryLimitDialog(
            currentLimit = linkHistoryLimit,
            onLimitSelected = viewModel::updateLinkHistoryLimit,
            onDismiss = viewModel::hideLinkHistoryLimitDialog
        )
    }

    if (showClearHistoryDialog) {
        ClearHistoryDialog(
            onConfirm = viewModel::clearLinkHistory,
            onDismiss = viewModel::hideClearHistoryDialog
        )
    }

    SaneScaffoldSettingsPage(headline = stringResource(id = R.string.privacy), onBackPressed = onBackPressed) {
        group(1) {
            item(key = R.string.show_linksheet_referrer) { padding, shape ->
                PreferenceSwitchListItem(
                    statePreference = showAsReferrer,
                    shape = shape,
                    padding = padding,
                    headlineContent = textContent(R.string.show_linksheet_referrer),
                    supportingContent = textContent(R.string.show_linksheet_referrer_explainer),
                )
            }
        }

        divider(key = "link_history_title", id = R.string.link_history_management)

        group(2) {
            item(key = "link_history_enabled") { padding, shape ->
                PreferenceSwitchListItem(
                    statePreference = enableLinkHistory,
                    shape = shape,
                    padding = padding,
                    headlineContent = textContent(R.string.enable_link_history),
                    supportingContent = textContent(R.string.enable_link_history_explainer),
                )
            }

            item(key = "link_history_duration") { padding, shape ->
                ClickableShapeListItem(
                    shape = shape,
                    padding = padding,
                    headlineContent = textContent(R.string.link_history_duration),
                    supportingContent = textContent(viewModel.getLinkHistoryDurationText()),
                    onClick = { viewModel.showLinkHistoryDurationDialog() },
                    role = Role.Button
                )
            }

            item(key = "link_history_limit") { padding, shape ->
                ClickableShapeListItem(
                    shape = shape,
                    padding = padding,
                    headlineContent = textContent(R.string.link_history_limit),
                    supportingContent = textContent(viewModel.getLinkHistoryLimitText()),
                    onClick = { viewModel.showLinkHistoryLimitDialog() },
                    role = Role.Button
                )
            }

            item(key = "view_link_history") { padding, shape ->
                ClickableShapeListItem(
                    shape = shape,
                    padding = padding,
                    headlineContent = textContent(R.string.view_link_history),
                    supportingContent = textContent(R.string.view_link_history_explainer),
                    onClick = { navigate?.invoke("history") },
                    role = Role.Button,
                    enabled = navigate != null
                )
            }

            item(key = "clear_link_history") { padding, shape ->
                ClickableShapeListItem(
                    shape = shape,
                    padding = padding,
                    headlineContent = textContent(R.string.link_history_clear),
                    supportingContent = textContent(R.string.link_history_clear_explainer),
                    onClick = { viewModel.showClearHistoryDialog() },
                    role = Role.Button
                )
            }
        }

        if (Build.IsDebug) {
            divider(key = R.string.telemetry_configure_title, id = R.string.telemetry_configure_title)

            group(2) {
                item(key = R.string.telemetry_configure_type) { padding, shape ->
                    ClickableShapeListItem(
                        shape = shape,
                        padding = padding,
                        onClick = { /* Analytics dialog removed */ },
                        role = Role.Button,
                        headlineContent = textContent(R.string.telemetry_configure_type),
                        supportingContent = textContent(telemetryLevel.titleId)
                    )
                }

                item(key = R.string.telemetry_identifier_reset) { padding, shape ->
                    ClickableShapeListItem(
                        shape = shape,
                        padding = padding,
                        onClick = { viewModel.resetIdentifier() },
                        role = Role.Button,
                        headlineContent = textContent(R.string.telemetry_identifier_reset)
                    )
                }
            }
        }
    }
}
