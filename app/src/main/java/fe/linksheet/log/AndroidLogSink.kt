package fe.linksheet.log

class AndroidLogSink : LogSink {
    override fun log(
        level: LLog.Level,
        tag: String?,
        throwable: Throwable?,
        message: String
    ) {
        val logMessage: String = throwable?.let { "$message\n${it.stackTraceToString()}" } ?: message

        android.util.Log.println(level.value, tag, logMessage)
    }
}
