package fe.linksheet.activity

import android.os.Bundle

// Must not be moved or renamed since LinkSheetCompat hardcodes the package/name
class BottomSheetActivity : BaseComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        // Minimal placeholder to satisfy build; full bottom sheet implementation is disabled in this build.
=======

        lifecycleScope.launch {
            viewModel.resolveResultFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .mapNotNull(::maybeHandleResult)
                .collectLatest(::handleLaunch)
        }

        lifecycleScope.launch {
            viewModel.warmupAsync()
        }

        setInitialIntent(intent)
        setContent(edgeToEdge = true) {
            AppTheme { Wrapper() }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Wrapper() {
        val event by viewModel.events.collectOnIO()
        val interaction by viewModel.interactions.collectOnIO()

        val resolveResult by viewModel.resolveResultFlow.collectAsStateWithLifecycle()
        val currentIntent by intentFlow.collectAsStateWithLifecycle()

        val coroutineScope = rememberCoroutineScope()
        val sheetState = rememberM3FixModalBottomSheetState()
//        val sheetState = rememberModalBottomSheetState(
////            confirmValueChange = {
////                if(it == SheetValue.Hidden) true else true
////            }
//        )

        LaunchedEffect(key1 = resolveResult) {
            logger.info("Expanding bottom sheet, status: $resolveResult, isPending=${resolveResult == IntentResolveResult.Pending}")
            if (resolveResult != IntentResolveResult.Pending) {
                // Need to do this in a separate effect as otherwise the preview image seems to mess up the layout-ing
                if (viewModel.improvedBottomSheetExpandFully.value) {
                    sheetState.expand()
                } else {
                    sheetState.partialExpand()
                }
            }
        }

        LaunchedEffect(key1 = event) {
            logger.info("Latest event: $event")
        }

        val controller = remember {
            DefaultBottomSheetStateController(
                activity = this@BottomSheetActivity,
                editorLauncher = editorLauncher,
                coroutineScope = coroutineScope,
                drawerState = sheetState,
                onNewIntent = ::onNewIntent,
                dispatch = { interaction ->
                    coroutineScope.launch {
                        (resolveResult as? IntentResolveResult.Default)
                            ?.let { viewModel.handle(this@BottomSheetActivity, it, interaction) }
                            ?.let { handleLaunch(it) }
                    }
                }
            )
        }

        val themeAmoled by viewModel.themeAmoled.collectAsStateWithLifecycle()
        val interceptAccidentalTaps by viewModel.interceptAccidentalTaps.collectAsStateWithLifecycle()
        val debug by LocalUiDebug.current.drawBorders.collectAsStateWithLifecycle()
        M3FixModalBottomSheet(
            contentModifier = Modifier
                .interceptTaps(sheetState, interceptAccidentalTaps)
                .debugBorder(debug, 1.dp, Color.Red),
            debug = debug,
            // TODO: Replace with pref
            isBlackTheme = themeAmoled,
            sheetState = sheetState,
            shape = RoundedCornerShape(
                topStart = 22.0.dp,
                topEnd = 22.0.dp,
                bottomEnd = 0.0.dp,
                bottomStart = 0.0.dp
            ),
            hide = {
                finish()
            },
            sheetContent = { modifier ->
                SheetContent(resolveResult, modifier, event, interaction, coroutineScope, sheetState, controller)
            }
        )
    }

    @Composable
    private fun SheetContent(
        resolveResult: IntentResolveResult,
        modifier: Modifier,
        event: ResolveEvent,
        interaction: ResolverInteraction,
        coroutineScope: CoroutineScope,
        sheetState: CompatSheetState,
        controller: BottomSheetStateController,
    ) {
        when (resolveResult) {
            is IntentResolveResult.Pending -> {
                val expressiveLoadingSheet by viewModel.expressiveLoadingSheet.collectAsStateWithLifecycle()
                LoadingIndicatorWrapper(
                    expressiveLoadingSheet = expressiveLoadingSheet,
                    event = event,
                    interaction = interaction,
                    requestExpand = {
                        logger.info("Loading indicator: Pre-Request expand")
                        coroutineScope.launch {
                            logger.info("Loading indicator: Request expand")
                            sheetState.expand()
                        }
                    }
                )
            }

            is IntentResolveResult.Default -> {
                val enableIgnoreLibRedirectButton by viewModel.enableIgnoreLibRedirectButton.collectAsStateWithLifecycle()
                val bottomSheetProfileSwitcher by viewModel.bottomSheetProfileSwitcher.collectAsStateWithLifecycle()
                val urlCopiedToast by viewModel.urlCopiedToast.collectAsStateWithLifecycle()
                val downloadStartedToast by viewModel.downloadStartedToast.collectAsStateWithLifecycle()
                val hideAfterCopying by viewModel.hideAfterCopying.collectAsStateWithLifecycle()
                val bottomSheetNativeLabel by viewModel.bottomSheetNativeLabel.collectAsStateWithLifecycle()
                val gridLayout by viewModel.gridLayout.collectAsStateWithLifecycle()
                val previewUrl by viewModel.previewUrl.collectAsStateWithLifecycle()
                val hideBottomSheetChoiceButtons by viewModel.hideBottomSheetChoiceButtons.collectAsStateWithLifecycle()
                val alwaysShowPackageName by viewModel.alwaysShowPackageName.collectAsStateWithLifecycle()
                val manualFollowRedirects by viewModel.manualFollowRedirects.collectAsStateWithLifecycle()
                val improvedBottomSheetUrlDoubleTap by viewModel.improvedBottomSheetUrlDoubleTap.collectAsStateWithLifecycle()

                BottomSheetApps(
                    modifier = modifier,
                    result = resolveResult,
                    imageLoader = viewModel.imageLoader,
                    enableIgnoreLibRedirectButton = enableIgnoreLibRedirectButton,
                    enableSwitchProfile = bottomSheetProfileSwitcher,
                    profileSwitcher = viewModel.profileSwitcher,
                    enableUrlCopiedToast = urlCopiedToast,
                    enableDownloadStartedToast = downloadStartedToast,
                    enableManualRedirect = manualFollowRedirects,
                    hideAfterCopying = hideAfterCopying,
                    bottomSheetNativeLabel = bottomSheetNativeLabel,
                    gridLayout = gridLayout,
                    appListSelectedIdx = viewModel.appListSelectedIdx.intValue,
                    copyUrl = { label, url ->
                        viewModel.clipboardManager.setText(label, url)
                    },
                    startDownload = { url, downloadable ->
                        viewModel.startDownload(resources, url, downloadable)
                    },
                    isPrivateBrowser = ::isPrivateBrowser,
                    showToast = { textId, duration, _ ->
                        coroutineScope.launch { showToast(textId = textId, duration = duration) }
                    },
                    controller = controller,
                    showPackage = alwaysShowPackageName,
                    previewUrl = previewUrl,
                    hideBottomSheetChoiceButtons = hideBottomSheetChoiceButtons,
                    urlCardDoubleTap = improvedBottomSheetUrlDoubleTap
                )
            }

            is IntentResolveResult.IntentParseFailed -> {
                FailureSheetContentWrapper(
                    modifier = modifier,
                    exception = resolveResult.exception,
                    onShareClick = {},
                    onCopyClick = {},
                    onSearchClick = {

                    }
                )
            }

            is IntentResolveResult.ResolveUrlFailed, is IntentResolveResult.UrlModificationFailed -> {}
            is IntentResolveResult.WebSearch -> {}
            IntentResolveResult.NoScenarioFound -> {}
            else -> {}
        }
    }

    private suspend fun showToast(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
        val text = getString(textId)
        withContext(Dispatchers.Main) {
            Toast.makeText(this@BottomSheetActivity, text, duration).show()
        }
    }

    private fun isPrivateBrowser(hasUri: Boolean, info: ActivityAppInfo): KnownBrowser? {
        if (!viewModel.enableRequestPrivateBrowsingButton.value || !hasUri) return null
        return KnownBrowser.isKnownBrowser(info.packageName, privateOnly = true)
    }

    private suspend fun maybeHandleResult(result: IntentResolveResult?): LaunchIntent? {
        return when (result) {
            is IntentResolveResult.Default if result.hasAutoLaunchApp && result.app != null -> {
                viewModel.makeOpenAppIntent(
                    result.app,
                    result.intent,
                    referrer,
                    result.isRegularPreferredApp,
                    null,
                    false
                )
            }
            is IntentResolveResult.IntentResult -> LaunchRawIntent(result.intent)
            else -> null
        }
    }

    private suspend fun handleLaunch(intent: LaunchIntent) {
        val result = launchHandler.start(intent.intent)
        if (result !is LaunchFailure) return

        logger.error(result.ex, "Launch failed: $result")
        val textId = when (result) {
            is LaunchResult.Illegal -> R.string.bottom_sheet__text_launch_illegal
            is LaunchResult.NotAllowed -> R.string.bottom_sheet__text_launch_not_allowed
            is LaunchResult.Other -> R.string.bottom_sheet__text_launch_failure_other
            is LaunchResult.Unknown -> R.string.bottom_sheet__text_launch_failure_unknown
            is LaunchResult.NotFound -> R.string.resolve_activity_failure
        }

        showToast(textId)
    }

    override fun onStop() {
        super.onStop()
        logger.info("onStop")

>>>>>>> 77b99c2077b8dfa56f994c5d1087e74867e7da51
        finish()
    }
}
