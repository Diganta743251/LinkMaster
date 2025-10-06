package fe.linksheet.module.database.entity

import android.content.ComponentName

data class PreferredApp(
    val host: String,
    val _packageName: String? = null,
    val _component: String? = null,
    val alwaysPreferred: Boolean = false,
)

object PreferredAppFactory {
    fun new(host: String, pkg: String, cmp: ComponentName?, always: Boolean): PreferredApp {
        return PreferredApp(
            host = host,
            _packageName = pkg,
            _component = cmp?.flattenToString(),
            alwaysPreferred = always
        )
    }
}
