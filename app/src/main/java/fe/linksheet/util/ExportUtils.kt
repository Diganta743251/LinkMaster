package fe.linksheet.util

fun buildExportText(entries: List<LogEntry>): String {
    return entries.joinToString("\n") { it.message }
}
