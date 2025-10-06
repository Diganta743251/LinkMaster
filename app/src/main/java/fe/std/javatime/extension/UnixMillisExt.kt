package fe.std.javatime.extension

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

val Long.unixMillisUtc: ZonedDateTime
    get() = Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC)
