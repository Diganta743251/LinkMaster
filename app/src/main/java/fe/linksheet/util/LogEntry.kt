package fe.linksheet.util

data class LogEntry(
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
