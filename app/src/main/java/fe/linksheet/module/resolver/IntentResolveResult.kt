package fe.linksheet.module.resolver

import android.content.Intent
import android.net.Uri
import fe.linksheet.feature.app.ActivityAppInfo
import fe.linksheet.module.downloader.DownloadCheckResult
import me.saket.unfurl.UnfurlResult

sealed interface IntentResolveResult {
    data object Pending : IntentResolveResult

    data class WebSearch(
        val query: String,
        val newIntent: Intent,
        val resolvedList: List<ActivityAppInfo>,
    ) : IntentResolveResult

    class IntentResult(val intent: Intent) : IntentResolveResult
    class OtherProfile(val url: String) : IntentResolveResult

    class Default(
        val intent: Intent,
        val uri: Uri?,
        val unfurlResult: UnfurlResult?,
        val referringPackageName: String?,
        val resolved: List<ActivityAppInfo>,
        val filteredItem: ActivityAppInfo?,
        val alwaysPreferred: Boolean?,
        val hasSingleMatchingOption: Boolean = false,
        val resolveModuleStatus: ResolveModuleStatus = ResolveModuleStatus(),
        val libRedirectResult: LibRedirectResult? = null,
        val downloadable: DownloadCheckResult? = DownloadCheckResult.NonDownloadable,
    ) : IntentResolveResult {
        val isRegularPreferredApp = alwaysPreferred == true && filteredItem != null
        val app: ActivityAppInfo? = filteredItem ?: resolved.firstOrNull()
        val hasAutoLaunchApp: Boolean = (isRegularPreferredApp || hasSingleMatchingOption) &&
            (referringPackageName == null || app?.packageName != referringPackageName)
    }

    data class IntentParseFailed(val exception: fe.linksheet.util.intent.parser.UriException) : IntentResolveResult
    data object UrlModificationFailed : IntentResolveResult
    data object ResolveUrlFailed : IntentResolveResult
    data object NoTrackFound : IntentResolveResult
}

class ResolveModuleStatus
