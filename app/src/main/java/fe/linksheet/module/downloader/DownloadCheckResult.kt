package fe.linksheet.module.downloader

sealed interface DownloadCheckResult {
    data class Downloadable(val fileName: String, val extension: String?) : DownloadCheckResult
    data object MimeTypeDetectionFailed : DownloadCheckResult
    data object NonDownloadable : DownloadCheckResult
}

fun DownloadCheckResult.isDownloadable(): Boolean = this is DownloadCheckResult.Downloadable
