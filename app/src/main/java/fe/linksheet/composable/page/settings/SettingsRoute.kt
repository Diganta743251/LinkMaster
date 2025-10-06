package fe.linksheet.composable.page.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fe.linksheet.composable.ui.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fe.android.compose.icon.iconPainter
import fe.android.compose.text.DefaultContent.Companion.text
import fe.android.compose.text.StringResourceContent.Companion.textContent
import fe.composekit.component.list.item.RouteNavItem
import fe.composekit.component.list.item.RouteNavigateListItem
import fe.composekit.component.list.item.default.DefaultTwoLineIconClickableShapeListItem
import fe.composekit.layout.column.group
import fe.composekit.preference.collectAsStateWithLifecycle
import fe.composekit.route.Route
import fe.composekit.route.RouteNavItemNew
import fe.composekit.route.RouteNavigateListItemNew
import fe.linksheet.R
import fe.linksheet.composable.component.page.SaneScaffoldSettingsPage
import fe.linksheet.composable.page.settings.language.rememberLanguageDialog
import fe.linksheet.composable.page.settings.scenario.ScenarioRoute
import fe.linksheet.module.language.LocaleItem
import fe.linksheet.module.viewmodel.SettingsViewModel
import fe.linksheet.navigation.*
import org.koin.androidx.compose.koinViewModel

internal object SettingsRouteData {
    val verifiedApps = arrayOf(
        RouteNavItemNew(
            AppsWhichCanOpenLinksSettingsRoute,
            Icons.Outlined.DomainVerification.iconPainter,
            textContent(R.string.verified_link_handlers),
            textContent(R.string.verified_link_handlers_subtitle)
        ),
//        RouteNavItemNew(
//            ScenarioRoute,
//            Icons.Outlined.Widgets.iconPainter,
//            textContent(R.string.settings_scenario__title_scenarios),
//            textContent(R.string.settings_scenario__text_scenarios),
//        )
    )

    val customization = arrayOf(
        RouteNavItem(
            browserSettingsRoute,
            Icons.Outlined.Apps.iconPainter,
            textContent(R.string.app_browsers),
            textContent(R.string.app_browsers_subtitle),
        ),
        RouteNavItem(
            bottomSheetSettingsRoute,
            Icons.Outlined.SwipeUp.iconPainter,
            textContent(R.string.bottom_sheet),
            textContent(R.string.bottom_sheet_explainer),
        ),
        RouteNavItem(
            linksSettingsRoute,
            Icons.Outlined.Link.iconPainter,
            textContent(R.string.links),
            textContent(R.string.links_explainer),
        )
    )

    val miscellaneous = arrayOf(
        RouteNavItem(
            generalSettingsRoute,
            Icons.Outlined.Settings.iconPainter,
            textContent(R.string.general),
            textContent(R.string.general_settings_explainer),
        ),
        RouteNavItem(
            notificationSettingsRoute,
            Icons.Outlined.Notifications.iconPainter,
            textContent(R.string.notifications),
            textContent(R.string.notifications_explainer),
        ),
        RouteNavItem(
            themeSettingsRoute,
            Icons.Outlined.Palette.iconPainter,
            textContent(R.string.theme),
            textContent(R.string.theme_explainer),
        ),
        RouteNavItem(
            linkMasterSettingsRoute,
            Icons.Outlined.QrCode.iconPainter,
            textContent(R.string.linkmaster_settings),
            textContent(R.string.linkmaster_settings_explainer),
        )
    )

    val languageRoute = RouteNavItemNew(
        LanguageRoute,
        Icons.Outlined.Language.iconPainter,
        textContent(R.string.settings_language__dialog_title),
        textContent(R.string.settings_language__text_system_language_with_language),
    )

    val privacyRoute = RouteNavItem(
        privacySettingsRoute,
        Icons.Outlined.PrivacyTip.iconPainter,
        textContent(R.string.privacy),
        textContent(R.string.privacy_settings_explainer),
    )

    val advanced = arrayOf(
        RouteNavItemNew(
            AdvancedRoute,
            Icons.Outlined.Terminal.iconPainter,
            textContent(R.string.advanced),
            textContent(R.string.settings__subtitle_advanced),
        )
        // Debug route removed - violates Play Store policies
    )

    // Dev mode removed - violates Play Store policies

    val about = arrayOf(
//        RouteNavItem(
//            Routes.Help,
//            Icons.AutoMirrored.Outlined.HelpOutline.iconPainter,
//            textContent(R.string.help),
//            textContent(R.string.help_subtitle),
//        ),
//        RouteNavItem(
//            Routes.Shortcuts,
//            Icons.Outlined.SwitchAccessShortcut.iconPainter,
//            textContent(R.string.settings__title_shortcuts),
//            textContent(R.string.settings__subtitle_shortcuts),
//        ),
//        RouteNavItem(
//            Routes.Updates,
//            Icons.Outlined.Update.iconPainter,
//            textContent(R.string.settings__title_updates),
//            textContent(R.string.settings__subtitle_updates),
//        ),
        RouteNavItem(
            aboutSettingsRoute,
            Icons.Outlined.Info.iconPainter,
            textContent(R.string.about),
            textContent(R.string.about_explainer),
        )
    )
}

@Composable
fun SettingsRoute(
    onBackPressed: () -> Unit,
    navigate: (String) -> Unit,
    navigateNew: (Route) -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val devMode by viewModel.devModeEnabled.collectAsStateWithLifecycle()
    val languageDialog = rememberLanguageDialog()

    SaneScaffoldSettingsPage(
        headline = stringResource(id = R.string.settings),
        onBackPressed = onBackPressed
    ) {
<<<<<<< HEAD
        // Modern hero section for settings
        item(key = "settings_hero", contentType = ContentType.SingleGroupItem) {
            AnimatedVisibility(
                visible = true,
                enter = ModernAnimations.SlideInFromBottom,
                exit = ModernAnimations.SlideOutToBottom
            ) {
                Modern2025HeroSection(
                    title = "Settings",
                    subtitle = "Customize your LinkMaster experience",
                    onActionClick = { /* Quick setup action */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
        
        item(key = R.string.verified_link_handlers, contentType = ContentType.SingleGroupItem) {
            AnimatedVisibility(
                visible = true,
                enter = ModernAnimations.SlideInFromRight + fadeIn(
                    animationSpec = tween(300, delayMillis = 100)
                ),
                exit = ModernAnimations.SlideOutToRight
            ) {
                ModernSettingsItem(
                    title = stringResource(R.string.verified_link_handlers),
                    subtitle = stringResource(R.string.verified_link_handlers_subtitle),
                    icon = Icons.Outlined.DomainVerification,
                    onClick = { navigate(SettingsRouteData.verifiedApps.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
=======
        group(array = SettingsRouteData.verifiedApps) { data, padding, shape ->
            RouteNavigateListItemNew(data = data, padding = padding, shape = shape, navigate = navigateNew)
>>>>>>> 77b99c2077b8dfa56f994c5d1087e74867e7da51
        }

        divider(id = R.string.customization)

        // Enhanced customization section with modern cards
        items(array = SettingsRouteData.customization) { data, index ->
            AnimatedVisibility(
                visible = true,
                enter = ModernAnimations.SlideInFromRight + fadeIn(
                    animationSpec = tween(300, delayMillis = 150 + (index * 50))
                ),
                exit = ModernAnimations.SlideOutToRight
            ) {
                ModernFeatureCard(
                    title = data.headline.text(),
                    description = data.subtitle.text(),
                    icon = when (data.route) {
                        browserSettingsRoute -> Icons.Outlined.Apps
                        bottomSheetSettingsRoute -> Icons.Outlined.SwipeUp
                        linksSettingsRoute -> Icons.Outlined.Link
                        else -> Icons.Outlined.Settings
                    },
                    onClick = { navigate(data.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    isHighlighted = data.route == browserSettingsRoute // Highlight browser settings
                )
            }
        }

        divider(id = R.string.misc_settings)

        group(size = SettingsRouteData.miscellaneous.size + 2) {
            items(array = SettingsRouteData.miscellaneous) { data, padding, shape ->
                RouteNavigateListItem(data = data, padding = padding, shape = shape, navigate = navigate)
            }

            item(key = SettingsRouteData.languageRoute.key) { padding, shape ->
                val appLocale by viewModel.currentLocale.collectAsStateWithLifecycle(
                    minActiveState = Lifecycle.State.RESUMED,
                    initialValue = null
                )
                LanguageListItem(
                    shape = shape,
                    padding = padding,
                    appLocale = appLocale?.first,
                    isSystemLanguage = appLocale?.second == true,
                    onClick = languageDialog::open
                )
            }

            item(key = privacySettingsRoute) { padding, shape ->
                RouteNavigateListItem(
                    data = SettingsRouteData.privacyRoute,
                    padding = padding,
                    shape = shape,
                    navigate = navigate
                )
            }
        }

        divider(id = R.string.advanced)

        group(size = SettingsRouteData.advanced.size) {
            items(array = SettingsRouteData.advanced) { data, padding, shape ->
                fe.composekit.route.RouteNavigateListItemNew(
                    data = data,
                    padding = padding,
                    shape = shape,
                    navigate = navigateNew
                )
            }
            // Dev mode section removed - violates Play Store policies
        }

        divider(id = R.string.about)

        group(array = SettingsRouteData.about) { data, padding, shape ->
            RouteNavigateListItem(data = data, padding = padding, shape = shape, navigate = navigate)
        }
    }
}

@Composable
private fun LanguageListItem(
    shape: Shape,
    padding: PaddingValues,
    appLocale: LocaleItem?,
    isSystemLanguage: Boolean,
    onClick: () -> Unit
) {
    DefaultTwoLineIconClickableShapeListItem(
        shape = shape,
        padding = padding,
        headlineContent = textContent(R.string.settings_language__dialog_title),
        supportingContent = when {
            appLocale != null && isSystemLanguage -> {
                textContent(
                    R.string.settings_language__text_system_language_with_language,
                    appLocale.displayName
                )
            }
            else -> text(appLocale?.displayName ?: "")
        },
        icon = Icons.Outlined.Language.iconPainter,
        onClick = onClick
    )
}
