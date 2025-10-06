package fe.linksheet.util

import android.content.Context
import android.net.Uri

object AndroidUri {
    fun get(scheme: Scheme, uri: Uri?): AndroidAppPackage? {
        return if (uri?.scheme == scheme.value) uri.host?.let { AndroidAppPackage(it) } else null
    }

    fun create(scheme: Scheme, context: Context): Uri {
        return Uri.fromParts(scheme.value, context.packageName, null)
    }

    fun create(scheme: Scheme, packageName: String): Uri {
        return Uri.fromParts(scheme.value, packageName, null)
    }
}

@JvmInline
value class AndroidAppPackage(val packageName: String)

fun Uri.getAndroidAppPackage(scheme: Scheme): AndroidAppPackage? {
    return AndroidUri.get(scheme, this)
}
