package fe.std.time

import java.time.LocalDate
import java.time.ZoneOffset

fun unixMillisOf(year: Int, month: Int = 1, day: Int = 1): Long {
    return LocalDate.of(year, month, day).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
}
