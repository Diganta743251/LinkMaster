package mozilla.components.support.utils

import android.content.Intent
import android.net.Uri
import android.os.Bundle

class SafeIntent(private val inner: Intent) {
    val action: String? get() = inner.action
    val data: Uri? get() = inner.data
    val extras: Bundle? get() = inner.extras

    fun getStringExtra(name: String): String? = inner.getStringExtra(name)
    fun getCharSequenceExtra(name: String): CharSequence? = inner.getCharSequenceExtra(name)
    fun getBooleanExtra(name: String, defaultValue: Boolean): Boolean = inner.getBooleanExtra(name, defaultValue)
}

fun Intent.toSafeIntent(): SafeIntent = SafeIntent(this)
