package fe.linksheet.extension.koin

class Logger {
    fun debug(message: String) {}
    fun debug(tag: String, message: String) {}
    fun info(message: String) {}
    fun error(message: String) {}
    fun error(t: Throwable) {}
    fun error(message: String, t: Throwable?) {}
    fun fatal(message: String) {}
}

inline fun <reified T> injectLogger(): Lazy<Logger> = lazy { Logger() }
