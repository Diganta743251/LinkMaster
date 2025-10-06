package fe.linksheet.util

import android.net.Uri

sealed class Scheme(val value: String) {
    data object Package : Scheme("package")
}

fun Scheme.create(part: String): Uri = Uri.parse("${this.value}:$part")
