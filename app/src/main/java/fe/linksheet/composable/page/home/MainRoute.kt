package fe.linksheet.composable.page.home

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fe.linksheet.composable.ui.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import fe.composekit.component.ContentType
import fe.composekit.component.list.column.SaneLazyColumnLayout
import fe.composekit.preference.collectAsStateWithLifecycle
import fe.linksheet.R
import fe.linksheet.composable.page.home.card.NightlyExperimentsCard
import fe.linksheet.composable.page.home.card.OpenCopiedLink
import fe.linksheet.composable.page.home.card.compat.MiuiCompatCardWrapper
import fe.linksheet.composable.page.home.card.news.ExperimentUpdatedCard
import fe.linksheet.composable.page.home.card.status.StatusCardWrapper
import fe.linksheet.composable.component.card.PlayStoreComplianceCard
import fe.linksheet.composable.page.home.card.ModernHistoryNavigationCard
import fe.linksheet.composable.ui.HkGroteskFontFamily
import fe.linksheet.extension.android.showToast
import fe.linksheet.extension.compose.ObserveClipboard
import fe.linksheet.extension.compose.OnFocused
import fe.linksheet.extension.kotlinx.collectRefreshableAsStateWithLifecycle
import fe.linksheet.module.viewmodel.MainViewModel
import fe.linksheet.navigation.MarkdownViewerRoute
import fe.linksheet.navigation.settingsRoute
import fe.linksheet.util.LinkSheet
import fe.linksheet.util.buildconfig.Build
import fe.linksheet.util.buildconfig.BuildType
import fe.linksheet.util.buildconfig.LinkSheetAppConfig
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMainRoute(navController: NavHostController, viewModel: MainViewModel = koinViewModel()) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val clipboardUri by viewModel.clipboardContent.collectAsStateWithLifecycle()
    val newDefaultsDismissed by viewModel.newDefaultsDismissed.collectAsStateWithLifecycle()

    val showMiuiAlert by viewModel.showMiuiAlert.collectRefreshableAsStateWithLifecycle(
        minActiveState = Lifecycle.State.RESUMED,
        initialValue = false
    )

    val defaultBrowser by viewModel.defaultBrowser.collectAsStateWithLifecycle(
        minActiveState = Lifecycle.State.RESUMED,
        initialValue = true
    )

    clipboardManager.ObserveClipboard {
        viewModel.tryReadClipboard()
    }

    LocalWindowInfo.current.OnFocused {
        viewModel.tryReadClipboard()
    }

//    var shizukuInstalled by remember { mutableStateOf(ShizukuUtil.isShizukuInstalled(context)) }
//    var shizukuRunning by remember { mutableStateOf(ShizukuUtil.isShizukuRunning()) }
//    LocalLifecycleOwner.current.lifecycle.ObserveStateChange(observeEvents = focusGainedEvents) {
//        shizukuInstalled = ShizukuUtil.isShizukuInstalled(context)
//        shizukuRunning = ShizukuUtil.isShizukuRunning()
//    }
    val activity = LocalActivity.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Modern transparent top bar with glassmorphism effect
            TopAppBar(
                title = {},
                navigationIcon = {
                    Modern3DButton(
                        onClick = { navController.navigate(settingsRoute) },
                        modifier = Modifier
                            .bouncyTap()
                            .size(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(id = R.string.settings),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        SaneLazyColumnLayout(
            padding = padding, 
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Modern hero section with animated title
            item(key = R.string.app_name, contentType = ContentType.TextItem) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    // Animated app title with modern styling
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontFamily = HkGroteskFontFamily,
                            fontWeight = FontWeight.Bold,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        ),
                        modifier = Modifier
                            .animateContentSize()
                            .floatingAnimation(amplitude = 2.dp, duration = 4000)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Subtitle with modern styling
                    Text(
                        text = "Smart Link Management",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.animateContentSize()
                    )
                }
            }

            item(key = R.string.thanks_for_donating, contentType = ContentType.TextItem) {
                if (!LinkSheetAppConfig.showDonationBanner()) {
                    Text(text = stringResource(id = R.string.thanks_for_donating))
                } else if (!Build.IsDebug) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            if (viewModel.debugMenu.enabled) {
                item {
                    viewModel.debugMenu.SlotContent(navigate = navController::navigate)
                }
            }

            item(
                key = R.string.settings_main_setup_success__title_linksheet_setup_success,
                contentType = ContentType.ClickableAlert
            ) {
                // Enhanced status card with modern 3D effects
                AnimatedVisibility(
                    visible = true,
                    enter = ModernAnimations.SlideInFromBottom,
                    exit = ModernAnimations.SlideOutToBottom
                ) {
                    Modern3DCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .bouncyTap(),
                        elevation = if (defaultBrowser) 8.dp else 4.dp,
                        colors = CardDefaults.cardColors(
                            containerColor = if (defaultBrowser) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.errorContainer
                            }
                        )
                    ) {
                        StatusCardWrapper(
                            isDefaultBrowser = defaultBrowser,
                            launchIntent = { viewModel.launchIntent(activity, it) },
                            updateDefaultBrowser = {}
                        )
                    }
                }
            }

            // Play Store Compliance Card
            item(key = "play_store_compliance", contentType = ContentType.SingleGroupItem) {
                AnimatedVisibility(
                    visible = true,
                    enter = ModernAnimations.SlideInFromBottom,
                    exit = ModernAnimations.SlideOutToBottom
                ) {
                    PlayStoreComplianceCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }
            }

            if (showMiuiAlert) {
                item(key = "miui_alert", contentType = ContentType.ClickableAlert) {
                    AnimatedVisibility(
                        visible = showMiuiAlert,
                        enter = ModernAnimations.SlideInFromRight,
                        exit = ModernAnimations.SlideOutToRight
                    ) {
                        NeonGlowCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .bouncyTap(),
                            glowColor = MaterialTheme.colorScheme.tertiary
                        ) {
                            MiuiCompatCardWrapper(
                                navigate = navController::navigate,
                                onClick = {
                                    coroutineScope.launch {
                                        if (!viewModel.updateMiuiAutoStartAppOp(activity)) {
                                            activity?.showToast(
                                                textId = R.string.settings_main_miui_compat__text_request_failed,
                                                duration = Toast.LENGTH_LONG
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            if (BuildType.current == BuildType.Debug || BuildType.current == BuildType.Nightly) {
                item(key = R.string.nightly_experiments_card, contentType = ContentType.ClickableAlert) {
                    AnimatedVisibility(
                        visible = true,
                        enter = ModernAnimations.ScaleInWithBounce,
                        exit = ModernAnimations.ScaleOutWithFade
                    ) {
                        GlassmorphismCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .bouncyTap()
                                .pulseAnimation(minScale = 0.98f, maxScale = 1.01f, duration = 2000)
                        ) {
                            NightlyExperimentsCard(navigate = { navController.navigate(it) })
                        }
                    }
                }
            }

            // History Navigation Card
            item(
                key = "history_navigation_card",
                contentType = ContentType.ClickableAlert
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = ModernAnimations.SlideInFromBottom,
                    exit = ModernAnimations.SlideOutToBottom
                ) {
                    ModernHistoryNavigationCard(
                        onClick = { navController.navigate("history") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }
            }

            if (!newDefaultsDismissed) {
                item(
                    key = R.string.settings_main_experiment_news__title_experiment_state_updated,
                    contentType = ContentType.ClickableAlert
                ) {
                    AnimatedVisibility(
                        visible = !newDefaultsDismissed,
                        enter = ModernAnimations.SlideInFromBottom,
                        exit = ModernAnimations.SlideOutToBottom
                    ) {
                        Modern3DCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .bouncyTap(),
                            elevation = 6.dp,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            ExperimentUpdatedCard(
                                onClick = {
                                    navController.navigate(MarkdownViewerRoute(LinkSheet.WikiExperiments))
                                },
                                onDismiss = {
                                    viewModel.newDefaultsDismissed.update(true)
                                }
                            )
                        }
                    }
                }
            }

            // Modern History Navigation Card
            item(key = "history_navigation", contentType = ContentType.ClickableAlert) {
                AnimatedVisibility(
                    visible = true,
                    enter = ModernAnimations.SlideInFromLeft + fadeIn(
                        animationSpec = tween(400, delayMillis = 300)
                    ),
                    exit = ModernAnimations.SlideOutToLeft
                ) {
                    ModernHistoryNavigationCard(
                        onClick = { navController.navigate(fe.linksheet.navigation.HistoryRoute) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }
            }

            if (clipboardUri != null) {
                item(key = R.string.open_copied_link, contentType = ContentType.ClickableAlert) {
                    AnimatedVisibility(
                        visible = clipboardUri != null,
                        enter = ModernAnimations.SlideInFromBottom + fadeIn(
                            animationSpec = tween(400, delayMillis = 200)
                        ),
                        exit = ModernAnimations.SlideOutToBottom
                    ) {
                        NeonGlowCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .bouncyTap(),
                            glowColor = MaterialTheme.colorScheme.primary
                        ) {
                            OpenCopiedLink(
                                uri = clipboardUri!!,
                                navigate = { navController.navigate(it) }
                            )
                        }
                    }
                }
            }

//            divider(id = R.string.settings_main_news__text_header)
        }
    }
}

