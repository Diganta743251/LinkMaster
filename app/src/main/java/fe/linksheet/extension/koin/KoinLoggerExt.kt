package fe.linksheet.extension.koin

import fe.linksheet.log.Logger

inline fun <reified T> injectLogger(): Lazy<Logger> = lazy { Logger }
