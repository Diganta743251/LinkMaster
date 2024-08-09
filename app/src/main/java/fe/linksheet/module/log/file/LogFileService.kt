package fe.linksheet.module.log.file

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import fe.android.lifecycle.LifecycleService
import fe.gson.extension.io.fromJsonOrNull
import fe.gson.extension.io.toJson
import fe.kotlin.extension.primitive.unixMillisUtc
import fe.kotlin.extension.time.localizedString
import fe.kotlin.extension.time.unixMillis
import fe.linksheet.extension.koin.service
import fe.linksheet.module.log.file.entry.LogEntry
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.koin.dsl.module
import java.io.File
import java.time.LocalDateTime

val logFileServiceModule = module {
    service<LogFileService> {
        val logDir = LogFileService.getLogDir(applicationContext)
        LogFileService(logDir)
    }
}

class LogFileService(private val logDir: File) : LifecycleService {
    companion object {
        private const val LOG_DIR = "logs"
        const val FILE_EXT = "log"
        const val FILE_EXT_V2 = "json"

        fun getLogDir(context: Context): File {
            return context.getDir(LOG_DIR, Context.MODE_PRIVATE)
        }
    }

    @Parcelize
    data class LogFile(val file: File, val millis: Long) : Parcelable {
        @IgnoredOnParcel
        val localizedTime by lazy { millis.unixMillisUtc.value.localizedString() }

        companion object {
            fun new(logDir: File): LogFile {
                val millis = System.currentTimeMillis()
                return LogFile(File(logDir, "$millis.$FILE_EXT_V2"), millis)
            }
        }
    }

    val startupTime: LocalDateTime = LocalDateTime.now()
    val logEntries = mutableListOf<LogEntry>()

    fun getLogFiles(): List<LogFile> {
        return logDir.listFiles()
            ?.filter { it.length() > 0L }
            ?.sortedDescending()
            ?.mapNotNull {
                val millis = it.nameWithoutExtension.substringBefore(".").toLongOrNull() ?: return@mapNotNull null
                LogFile(it, millis)
            } ?: emptyList()
    }

    fun deleteLogFile(logFile: LogFile): Boolean {
        return logFile.file.delete()
    }

    fun readLogFile(name: String): List<LogEntry> {
        val logFile = File(logDir, name)

        if (logFile.extension == FILE_EXT) {
            return logFile
                .readLines().filter { it.isNotEmpty() }
                .mapNotNull { LogEntry.fromLogFileLine(it) }
        }

        return logFile.fromJsonOrNull<List<LogEntry?>>()?.filterNotNull() ?: emptyList()
    }

    fun write(entry: LogEntry) {
        logEntries.add(entry)
    }

    override suspend fun onAppInitialized(owner: LifecycleOwner) {
        val startupMillis = startupTime.minusWeeks(2).unixMillis.millis
        getLogFiles().filter { it.millis < startupMillis }.forEach { deleteLogFile(it) }
    }

    override suspend fun onStop() {
        val immutable = logEntries.toList()
        if (immutable.isNotEmpty()) {
            LogFile.new(logDir).file.toJson(immutable)
        }
    }
}
