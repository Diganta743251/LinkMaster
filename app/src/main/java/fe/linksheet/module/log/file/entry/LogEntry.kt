package fe.linksheet.module.log.file.entry

sealed class LogEntry {
    data class FatalEntry(
        val message: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : LogEntry()
    
    data class ErrorEntry(
        val message: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : LogEntry()
    
    data class WarningEntry(
        val message: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : LogEntry()
    
    data class InfoEntry(
        val message: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : LogEntry()
    
    data class DebugEntry(
        val message: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : LogEntry()
}
