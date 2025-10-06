package fe.linksheet.util.web

object HostUtil {
    private const val HTTPS_SCHEME = "https://"

    // TODO: Does not look very robust
    fun cleanHttpsScheme(host: String): String {
        val hostWithoutScheme = if (host.indexOf(HTTPS_SCHEME) != -1) {
            host.substring(HTTPS_SCHEME.length)
        } else host

        return if (hostWithoutScheme.endsWith("/")) hostWithoutScheme.substring(
            0,
            hostWithoutScheme.length - 1
        ) else hostWithoutScheme
    }
}
