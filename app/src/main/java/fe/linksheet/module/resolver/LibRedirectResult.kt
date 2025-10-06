package fe.linksheet.module.resolver

import android.net.Uri

sealed interface LibRedirectResult {
    class Redirected(val originalUri: Uri, val redirectedUri: Uri) : LibRedirectResult
    data object NotRedirected : LibRedirectResult
}
