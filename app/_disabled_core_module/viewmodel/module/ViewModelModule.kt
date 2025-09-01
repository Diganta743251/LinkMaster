package fe.linksheet.module.viewmodel.module


import fe.gson.GsonQualifier
import fe.linksheet.module.log.DefaultLogModule
import fe.linksheet.module.preference.PreferenceRepositoryModule
import fe.linksheet.module.profile.ProfileSwitcherModule
import fe.linksheet.module.repository.module.RepositoryModule
import fe.linksheet.module.viewmodel.*
import fe.linksheet.module.viewmodel.util.LogViewCommon
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val ViewModelModule = module {
    includes(
        PreferenceRepositoryModule,
        RepositoryModule,
        DefaultLogModule,
        ProfileSwitcherModule
    )
    single {
        LogViewCommon(
            preferenceRepository = get(),
            // experimentRepository = get(), // Removed - violates Play Store policies
            // pasteService = get(), // Removed - violates Play Store policies
            gson = get(qualifier(GsonQualifier.Pretty)),
            redactor = get(),
            systemInfoService = get()
        )
    }

    viewModel { 
        MainViewModel(
            context = get(),
            appStateRepository = get(),
            preferenceRepository = get(),
            // experimentRepository = get(), // Removed - violates Play Store policies
            // analyticsService = get(), // Removed - violates Play Store policies
            miuiCompatProvider = get(),
            miuiCompat = get(),
            // debugMenu = get(), // Removed - violates Play Store policies
            intentHandler = get()
            // workDelegatorService = get() // Removed - violates Play Store policies
        )
    }
    viewModel { 
        VerifiedLinkHandlersViewModel(
            // shizukuHandler = get(), // Removed - violates Play Store policies
            preferenceRepository = get(),
            // experimentRepository = get(), // Removed - violates Play Store policies
            preferredAppRepository = get(),
            packageInfoService = get(),
            intentCompat = get()
        )
    }
    
    viewModel { parameters ->
        VerifiedLinkHandlerViewModel(
            packageName  = parameters.get(),
            preferenceRepository = get(),
            preferredAppRepository = get(),
            packageService = get(),
            intentCompat = get()
        )
    }
    viewModel { 
        PreferredAppSettingsViewModel(
            context = get(),
            repository = get(),
            preferenceRepository = get(),
            packageInfoService = get()
        )
    }
    viewModel { 
        InAppBrowserSettingsViewModel(
            context = get(),
            repository = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        PreferredBrowserViewModel(
            context = get(),
            browserResolver = get(),
            normalBrowsersRepository = get(),
            inAppBrowsersRepository = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        BottomSheetSettingsViewModel(
            context = get(),
            preferenceRepository = get(),
            // experimentsRepository = get(), // Removed - violates Play Store policies
            profileSwitcher = get()
        )
    }
    viewModel { 
        LinksSettingsViewModel(
            context = get(),
            preferenceRepository = get()
        )
    }
    // LibRedirect ViewModels removed - URL processing violates Play Store policies
    viewModel { 
        BottomSheetViewModel(
            context = get(),
            preferenceRepository = get(),
            // experimentRepository = get(), // Removed - violates Play Store policies
            preferredAppRepository = get(),
            appSelectionHistoryRepository = get(),
            profileSwitcher = get(),
            intentResolver = get(),
            imageLoader = get(),
            intentLauncher = get()
        )
    }
    viewModel { 
        ThemeSettingsViewModel(
            // remoteConfigRepository = get(), // Removed - violates Play Store policies
            preferenceRepository = get()
        )
    }
    viewModel { 
        LanguageSettingsViewModel(
            localeService = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        FollowRedirectsSettingsViewModel(
            context = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        DownloaderSettingsViewModel(
            context = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        LogSettingsViewModel(
            preferenceRepository = get(),
            logPersistService = get()
        )
    }
    viewModel { parameters ->
        LogTextSettingsViewModel(
            context = get(),
            sessionId = parameters[0],
            logViewCommon = get(),
            preferenceRepository = get(),
            logPersistService = get()
        )
    }
    viewModel { 
        CrashHandlerViewerViewModel(
            context = get(),
            preferenceRepository = get(),
            // experimentsRepository = get(), // Removed - violates Play Store policies
            logViewCommon = get(),
            logPersistService = get()
        )
    }
    viewModel { 
        Amp2HtmlSettingsViewModel(
            context = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        FeatureFlagViewModel(
            context = get(),
            preferenceRepository = get(),
            featureFlagRepository = get()
        )
    }
    viewModel { 
        PretendToBeAppSettingsViewModel(
            context = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        GeneralSettingsViewModel(
            context = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        LoadDumpedPreferencesViewModel(
            context = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        PrivacySettingsViewModel(
            context = get(),
            preferenceRepository = get(),
            experimentsRepository = get(),
            analyticsService = get()
        )
    }
    viewModel {
        ExportSettingsViewModel(
            context = get(),
            preferenceRepository = get(),
            gson = get(qualifier(GsonQualifier.Pretty)),
            clock = get(),
            zoneId = get()
        )
    }
    viewModel {
        AboutSettingsViewModel(
            context = get(),
            gson = get(qualifier(GsonQualifier.Pretty)),
            preferenceRepository = get()
        )
    }
    // DevSettingsViewModel removed - violates Play Store policies
    viewModel { 
        SettingsViewModel(
            context = get(),
            localeService = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        NotificationSettingsViewModel(
            context = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        ExperimentsViewModel(
            context = get(),
            savedStateHandle = get(),
            preferenceRepository = get(),
            experimentRepository = get()
        )
    }
    viewModel { 
        AppConfigViewModel(
            context = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        ProfileSwitchingSettingsViewModel(
            context = get(),
            profileSwitcher = get(),
            preferenceRepository = get()
        )
    }
    viewModel { 
        MarkdownViewModel(
            context = get(),
            repository = get()
        )
    }
    viewModel { 
        SqlViewModel(database = get())
    }
    viewModel { 
        PreviewSettingsViewModel(
            context = get(),
            preferenceRepository = get()
        )
    }
    viewModel {
        HistoryViewModel(
            context = get(),
            appSelectionHistoryRepository = get()
        )
    }
    
    // Link Detail and QR Code ViewModels with parameters
    viewModel { parameters ->
        LinkDetailViewModel(
            context = get(),
            linkUrl = parameters.get()
        )
    }
    
    viewModel { parameters ->
        QRCodeViewModel(
            context = get(),
            linkUrl = parameters.get()
        )
    }
}
