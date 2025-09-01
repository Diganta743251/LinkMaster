package fe.linksheet.module.resolver

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

fun PackageManager.queryIntentActivitiesCompat(intent: android.content.Intent, flags: Int): List<ResolveInfo> {
    return queryIntentActivities(intent, flags)
}
