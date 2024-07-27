package fe.linksheet.composable.settings.about

import LibRedirectMetadata
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Euro
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fe.clearurlskt.ClearURLsMetadata
import fe.fastforwardkt.FastForwardRules
import fe.kotlin.extension.primitive.unixMillisUtc
import fe.kotlin.time.ISO8601DateTimeFormatter
import fe.linksheet.*
import fe.linksheet.composable.settings.SettingsScaffold
import fe.linksheet.composable.util.ColoredIcon
import fe.linksheet.composable.util.PreferenceSubtitle
import fe.linksheet.composable.util.SettingsItemRow
import fe.linksheet.composable.util.SubtitleText
import fe.linksheet.composable.util.rememberAnnotatedStringResource
import fe.linksheet.extension.android.showToast
import fe.linksheet.extension.compose.currentActivity
import fe.linksheet.module.viewmodel.AboutSettingsViewModel
import fe.linksheet.util.AppSignature
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutSettingsRoute(
    navController: NavHostController,
    onBackPressed: () -> Unit,
    viewModel: AboutSettingsViewModel = koinViewModel()
) {
    val activity = LocalContext.currentActivity()
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current
    val buildDate =
        BuildConfig.BUILT_AT.unixMillisUtc.format(ISO8601DateTimeFormatter.DefaultFormat)
    val buildType = AppSignature.checkSignature(activity)

    var devClicks by remember { mutableIntStateOf(0) }

    SettingsScaffold(R.string.about, onBackPressed = onBackPressed) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight(),
            contentPadding = PaddingValues(horizontal = 5.dp)
        ) {
            stickyHeader(key = "misc") {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                    PreferenceSubtitle(text = stringResource(R.string.misc_settings))
                }
            }

            item(creditsSettingsRoute) {
                SettingsItemRow(
                    navController = navController,
                    navigateTo = creditsSettingsRoute,
                    headlineId = R.string.credits,
                    subtitleId = R.string.credits_explainer,
                    image = {
                        ColoredIcon(icon = Icons.Default.Link, descriptionId = R.string.credits)
                    }
                )
            }

            item("github") {
                SettingsItemRow(
                    headlineId = R.string.github,
                    subtitleId = R.string.github_explainer,
                    onClick = {
                        uriHandler.openUri(linksheetGithub)
                    },
                    image = {
                        ColoredIcon(icon = Icons.Default.Home, descriptionId = R.string.github)
                    }
                )
            }

            item("discord") {
                SettingsItemRow(
                    headlineId = R.string.discord,
                    subtitleId = R.string.discord_explainer,
                    onClick = {
                        uriHandler.openUri(BuildConfig.LINK_DISCORD)
                    },
                    image = {
                        ColoredIcon(icon = Icons.Default.Chat, descriptionId = R.string.discord)
                    }
                )
            }

            stickyHeader(key = "donation") {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                    PreferenceSubtitle(text = stringResource(R.string.donation))
                }
            }

            if (LinkSheetAppConfig.showDonationBanner()) {
                item("donate") {
                    SettingsItemRow(
                        headline = stringResource(id = R.string.donate),
                        subtitle = rememberAnnotatedStringResource(id = R.string.donate_subtitle),
                        onClick = {
                            navController.navigate(donateSettingsRoute)
                        },
                        image = {
                            ColoredIcon(
                                icon = Icons.Default.Euro,
                                descriptionId = R.string.donate
                            )
                        }
                    )
                }
            }

            stickyHeader(key = "version") {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                    PreferenceSubtitle(text = stringResource(R.string.just_version))
                }
            }

            if (buildType != AppSignature.SignatureBuildType.Debug) {
                item("build_type") {
                    val isUnofficial = buildType == AppSignature.SignatureBuildType.Unofficial

                    SettingsItemRow(
                        headlineId = R.string.built_by,
                        subtitleId = buildType.stringRes,
                        image = {
                            ColoredIcon(
                                icon = if (isUnofficial) Icons.Default.Warning else Icons.Default.Build,
                                descriptionId = R.string.built_by,
                                color = if (isUnofficial) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
            }

            item("linksheet_version") {
                val builtAt = buildNameValueAnnotatedString(
                    stringResource(id = R.string.built_at),
                    buildDate
                )

//                val commit = buildNameValueAnnotatedString(
//                    stringResource(id = R.string.commit),
//                    BuildConfig.COMMIT.substring(0, 7)
//                )

//                val branch = buildNameValueAnnotatedString(
//                    stringResource(id = R.string.branch),
//                    BuildConfig.BRANCH
//                )

                val fullVersionName = buildNameValueAnnotatedString(
                    stringResource(id = R.string.version_name),
                    BuildConfig.VERSION_NAME
                )

                val flavor = buildNameValueAnnotatedString(
                    stringResource(id = R.string.flavor),
                    BuildConfig.FLAVOR
                )
//
//                val type = buildNameValueAnnotatedString(
//                    stringResource(id = R.string.type),
//                    BuildConfig.BUILD_TYPE
//                )

                val workflow = if (BuildConfig.GITHUB_WORKFLOW_RUN_ID != null) {
                    buildNameValueAnnotatedString(
                        stringResource(id = R.string.github_workflow_run_id),
                        BuildConfig.GITHUB_WORKFLOW_RUN_ID
                    )
                } else null

                SettingsItemRow(
                    headline = stringResource(id = R.string.version),
                    subtitle = builtAt,
                    onClick = {
                        if (devClicks == 7 && !viewModel.devModeEnabled()) {
                            viewModel.devModeEnabled(true)
                            activity.showToast(R.string.dev_mode_enabled, Toast.LENGTH_SHORT)
                        }

                        devClicks++
                    },
                    image = {
                        ColoredIcon(icon = Icons.Default.Link, descriptionId = R.string.version)
                    },
                    content = {
                        SubtitleText(subtitle = flavor)
//                        SubtitleText(subtitle = type)
//                        SubtitleText(subtitle = commit)
//                        SubtitleText(subtitle = branch)
                        SubtitleText(subtitle = fullVersionName)

                        if (workflow != null) {
                            SubtitleText(
                                subtitle = workflow
                            )
                        }
                    }
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = {
                        clipboardManager.setText(buildAnnotatedString {
                            append(
                                activity.getText(R.string.linksheet_version_info_header),
                                lineSeparator,
                                builtAt,
                                lineSeparator,
                                flavor,
                                lineSeparator,
//                                type,
//                                lineSeparator,
//                                commit,
//                                lineSeparator,
//                                branch,
//                                lineSeparator,
                                fullVersionName
                            )

                            if (workflow != null) {
                                append(lineSeparator, workflow)
                            }
                        })
                    }) {
                        Text(text = stringResource(id = R.string.copy_version_information))
                    }
                }
            }

            item("clearurls_version") {
                LibraryLastUpdatedRow(
                    R.string.clear_urls_version,
                    ClearURLsMetadata.FETCHED_AT,
                    Icons.Default.ClearAll
                )
            }

            item("fastforward_version") {
                LibraryLastUpdatedRow(
                    R.string.fastforward_version,
                    FastForwardRules.fetchedAt,
                    Icons.Default.Bolt
                )
            }

            item("libredirect") {
                LibraryLastUpdatedRow(
                    R.string.libredirect_version,
                    LibRedirectMetadata.fetchedAt,
                    Icons.Default.Security
                )
            }
        }
    }
}

@Composable
private fun LibraryLastUpdatedRow(@StringRes headline: Int, fetchedAt: Long, icon: ImageVector) {
    SettingsItemRow(
        headline = stringResource(id = headline),
        subtitle = buildNameValueAnnotatedString(
            stringResource(id = R.string.last_updated),
            fetchedAt.unixMillisUtc.format(ISO8601DateTimeFormatter.DefaultFormat)
        ),
        image = {
            ColoredIcon(
                icon = icon,
                descriptionId = headline
            )
        }
    )
}

private fun buildNameValueAnnotatedString(name: String, value: String): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(name)
        }

        append(" ", value)
    }
}
