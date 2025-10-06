package fe.linksheet.module.viewmodel

import androidx.lifecycle.ViewModel
import fe.linksheet.module.viewmodel.util.LogViewCommon
import fe.linksheet.module.log.file.entry.LogEntry

class CrashHandlerViewerViewModel : ViewModel() {
    val logViewCommon: LogViewCommon = LogViewCommon()
    val logPersistService: LogPersistService = LogPersistService()
}

class LogPersistService {
    fun readEntries(limit: Int?): List<LogEntry> = emptyList()
}
