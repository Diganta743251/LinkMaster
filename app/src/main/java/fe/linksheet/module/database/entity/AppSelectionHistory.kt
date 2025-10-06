package fe.linksheet.module.database.entity

import androidx.annotation.Keep

// Stub entity for Play Store compliance
@Keep
data class AppSelectionHistory(
    val id: Long = 0,
    val packageName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
