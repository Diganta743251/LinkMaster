package fe.linksheet.module.viewmodel.util

import android.content.Context
import fe.linksheet.module.log.file.entry.LogEntry

class LogViewCommon {
    data class ExportSettings(
        val includeFingerprint: Boolean,
        val includePreferences: Boolean,
        val redactLog: Boolean,
        val includeThrowable: Boolean
    )
    
    fun buildExportText(context: Context, settings: ExportSettings, logEntries: List<LogEntry>): String {
        return logEntries.joinToString("\n") { entry ->
            when (entry) {
                is LogEntry.FatalEntry -> "[FATAL] ${entry.message}"
                is LogEntry.ErrorEntry -> "[ERROR] ${entry.message}"
                is LogEntry.WarningEntry -> "[WARN] ${entry.message}"
                is LogEntry.InfoEntry -> "[INFO] ${entry.message}"
                is LogEntry.DebugEntry -> "[DEBUG] ${entry.message}"
            }
        }
    }
}
