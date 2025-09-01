package fe.linksheet.composable.page.settings.bottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import fe.android.compose.text.StringResourceContent.Companion.textContent
import fe.composekit.component.ContentType
import fe.linksheet.R
import fe.linksheet.composable.component.list.item.type.PreferenceSwitchListItem
import fe.linksheet.composable.component.page.SaneScaffoldSettingsPage
import fe.linksheet.module.viewmodel.BottomSheetSettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EnhancedBottomSheetSettingsRoute(
    onBackPressed: () -> Unit,
    viewModel: BottomSheetSettingsViewModel = koinViewModel()
) {
    SaneScaffoldSettingsPage(
        headline = stringResource(id = R.string.enhanced_bottom_sheet),
        onBackPressed = onBackPressed
    ) {
        group(size = 6) {
            item(key = R.string.enable_bottom_sheet_expand_fully, contentType = ContentType.SingleGroupItem) {
                PreferenceSwitchListItem(
                    statePreference = viewModel.enableBottomSheetExpandFully,
                    headlineContent = textContent(R.string.enable_bottom_sheet_expand_fully),
                    supportingContent = textContent(R.string.enable_bottom_sheet_expand_fully_description)
                )
            }

            item(key = R.string.enable_bottom_sheet_url_double_tap, contentType = ContentType.SingleGroupItem) {
                PreferenceSwitchListItem(
                    statePreference = viewModel.enableBottomSheetUrlDoubleTap,
                    headlineContent = textContent(R.string.enable_bottom_sheet_url_double_tap),
                    supportingContent = textContent(R.string.enable_bottom_sheet_url_double_tap_description)
                )
            }

            item(key = R.string.enable_intercept_accidental_taps, contentType = ContentType.SingleGroupItem) {
                PreferenceSwitchListItem(
                    statePreference = viewModel.enableInterceptAccidentalTaps,
                    headlineContent = textContent(R.string.enable_intercept_accidental_taps),
                    supportingContent = textContent(R.string.enable_intercept_accidental_taps_description)
                )
            }

            item(key = R.string.enable_manual_follow_redirects, contentType = ContentType.SingleGroupItem) {
                PreferenceSwitchListItem(
                    statePreference = viewModel.enableManualFollowRedirects,
                    headlineContent = textContent(R.string.enable_manual_follow_redirects),
                    supportingContent = textContent(R.string.enable_manual_follow_redirects_description)
                )
            }

            item(key = R.string.disable_bottom_sheet_state_save, contentType = ContentType.SingleGroupItem) {
                PreferenceSwitchListItem(
                    statePreference = viewModel.disableBottomSheetStateSave,
                    headlineContent = textContent(R.string.disable_bottom_sheet_state_save),
                    supportingContent = textContent(R.string.disable_bottom_sheet_state_save_description)
                )
            }

            item(key = R.string.enable_expressive_loading_sheet, contentType = ContentType.SingleGroupItem) {
                PreferenceSwitchListItem(
                    statePreference = viewModel.enableExpressiveLoadingSheet,
                    headlineContent = textContent(R.string.enable_expressive_loading_sheet),
                    supportingContent = textContent(R.string.enable_expressive_loading_sheet_description)
                )
            }
        }
    }
}