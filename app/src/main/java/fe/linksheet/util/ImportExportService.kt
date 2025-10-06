package fe.linksheet.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import fe.composekit.intent.buildIntent
import fe.linksheet.R
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

class ImportExportService(val context: Context, val clock: Clock, val zoneId: ZoneId) {
    companion object {
        val ImportIntent = buildIntent(Intent.ACTION_OPEN_DOCUMENT) {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }

        private val exportIntent = buildIntent(Intent.ACTION_CREATE_DOCUMENT) {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }

        private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm")
    }

    fun createExportIntent(now: Instant = clock.now()): Intent {
        val nowString = now.toJavaInstant().atZone(zoneId).format(dateTimeFormatter)
        return Intent(exportIntent)
            .putExtra(
                Intent.EXTRA_TITLE,
                context.getString(R.string.export_file_name, nowString)
            )
    }
    // Export/import functions are intentionally omitted in this build to remove
    // dependencies on extensions and result wrappers not present in this project.
}
