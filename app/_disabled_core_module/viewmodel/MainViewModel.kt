package fe.linksheet.module.viewmodel


import android.app.Activity
import android.app.Application
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.getSystemService
import androidx.navigation.NavDestination
import fe.linksheet.extension.android.getFirstText
import fe.linksheet.extension.android.setText
import fe.linksheet.extension.android.startActivityWithConfirmation
import fe.linksheet.extension.kotlinx.RefreshableStateFlow
// Analytics imports removed - violates Play Store policies
import fe.linksheet.feature.app.`package`.PackageIntentHandler
// Debug menu import removed - violates Play Store policies
import fe.linksheet.module.devicecompat.miui.MiuiCompat
import fe.linksheet.module.devicecompat.miui.MiuiCompatProvider
import fe.linksheet.module.preference.SensitivePreference
import fe.linksheet.module.preference.app.AppPreferenceRepository
import fe.linksheet.module.preference.app.AppPreferences
// Experiment repository import removed - violates Play Store policies
import fe.linksheet.module.preference.state.AppStatePreferences
import fe.linksheet.module.preference.state.AppStateRepository
import fe.linksheet.module.viewmodel.base.BaseViewModel
// Work manager import removed - violates Play Store policies
import fe.linksheet.util.web.UriUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow


class MainViewModel(
    val context: Application,
    val appStateRepository: AppStateRepository,
    val preferenceRepository: AppPreferenceRepository,
    // val experimentRepository: ExperimentRepository, // Removed - violates Play Store policies
    // private val analyticsService: BaseAnalyticsService, // Removed - violates Play Store policies
    private val miuiCompatProvider: MiuiCompatProvider,
    private val miuiCompat: MiuiCompat,
    // val debugMenu: DebugMenuSlotProvider, // Removed - violates Play Store policies
    private val intentHandler: PackageIntentHandler,
    // private val workDelegatorService: WorkDelegatorService, // Removed - violates Play Store policies
) : BaseViewModel(preferenceRepository) {
    val newDefaultsDismissed = appStateRepository.asViewModelState(AppStatePreferences.newDefaults_2024_12_29_InfoDismissed)

    // Telemetry, remote config, and experiment features removed - violate Play Store policies
    val homeClipboardCard = preferenceRepository.asViewModelState(AppPreferences.homeClipboardCard)

    private val clipboardManager by lazy { context.getSystemService<ClipboardManager>()!! }

    private val _clipboardContent = MutableStateFlow<Uri?>(null)
    val clipboardContent = _clipboardContent.asStateFlow()

    fun tryReadClipboard() {
        if (!homeClipboardCard.value) {
            _clipboardContent.tryEmit(null)
            return
        }

        val clipboardUri = clipboardManager.getFirstText()?.let { tryParseUriString(it) }
        if (clipboardUri != null && _clipboardContent.value != clipboardUri) {
            _clipboardContent.tryEmit(clipboardUri)
        }
    }

    fun tryUpdateClipboard(label: String, uriStr: String) {
        val uri = tryParseUriString(uriStr)
        if (uri != null) {
            clipboardManager.setText(label, uri.toString())
        }
    }

    private val _showMiuiAlert = RefreshableStateFlow(false) {
        if (miuiCompatProvider.isRequired.value) miuiCompat.showAlert(context) else false
    }

    val showMiuiAlert = _showMiuiAlert

    suspend fun updateMiuiAutoStartAppOp(activity: Activity?): Boolean {
        if (activity == null) return false
        val result = miuiCompat.startPermissionRequest(activity)
        _showMiuiAlert.refresh()

        return result
    }

    private val _defaultBrowser = { intentHandler.isSelfDefaultBrowser() }.asFlow()
    val defaultBrowser = _defaultBrowser

    fun launchIntent(activity: Activity?, intent: SettingsIntent): Boolean {
        if (activity == null) return false
        return activity.startActivityWithConfirmation(Intent(intent.action))
    }

    private fun tryParseUriString(uriStr: String): Uri? {
        return UriUtil.parseWebUriStrict(uriStr)
    }

    fun enqueueNavEvent(destination: NavDestination, args: Bundle?) {
        // Analytics removed - violates Play Store policies
    }

    fun updateTelemetryLevel(level: TelemetryLevel) {
        telemetryLevel.update(level)
        telemetryShowInfoDialog.update(false)
        // Analytics removed - violates Play Store policies
    }

    fun setRemoteConfig(enabled: Boolean) {
        remoteConfigDialogDismissed(true)
        remoteConfig(enabled)
        // Work manager removed - violates Play Store policies
    }

    enum class SettingsIntent(val action: String) {
        DefaultApps(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS),
        DomainUrls("android.settings.MANAGE_DOMAIN_URLS"),
        CrossProfileAccess("android.settings.MANAGE_CROSS_PROFILE_ACCESS")
    }
}
