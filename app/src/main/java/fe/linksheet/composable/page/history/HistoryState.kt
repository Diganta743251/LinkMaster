package fe.linksheet.composable.page.history

import fe.linksheet.module.database.entity.AppSelectionHistory

/**
 * Sealed class representing different states of the History screen
 */
sealed class HistoryState {
    object Loading : HistoryState()
    data class Success(
        val items: List<HistoryItem>,
        val totalCount: Int,
        val hasMore: Boolean = false
    ) : HistoryState()
    data class Error(
        val message: String,
        val canRetry: Boolean = true
    ) : HistoryState()
    object Empty : HistoryState()
}

/**
 * Sealed class for QR Code generation states
 */
sealed class QRCodeState {
    object Idle : QRCodeState()
    object Generating : QRCodeState()
    data class Success(
        val bitmap: android.graphics.Bitmap,
        val size: Int,
        val foregroundColor: Int,
        val backgroundColor: Int
    ) : QRCodeState()
    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : QRCodeState()
}

/**
 * Sealed class for sharing states
 */
sealed class SharingState {
    object Idle : SharingState()
    object Preparing : SharingState()
    object Success : SharingState()
    data class Error(val message: String) : SharingState()
}

/**
 * Data class for link summary generation state
 */
data class SummaryState(
    val isLoading: Boolean = false,
    val summary: String? = null,
    val error: String? = null,
    val lastUpdated: Long? = null
)

/**
 * Configuration for history display
 */
data class HistoryConfig(
    val pageSize: Int = 50,
    val enablePagination: Boolean = true,
    val enableSearch: Boolean = true,
    val enableSort: Boolean = true,
    val enableSelection: Boolean = true,
    val maxCacheSize: Int = 1000
)

/**
 * Performance metrics for monitoring
 */
data class PerformanceMetrics(
    val qrGenerationTimeMs: Long = 0,
    val historyLoadTimeMs: Long = 0,
    val summaryGenerationTimeMs: Long = 0,
    val memoryUsageMb: Float = 0f
)