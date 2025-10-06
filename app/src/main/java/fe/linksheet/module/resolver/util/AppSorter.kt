package fe.linksheet.module.resolver.util

import android.content.pm.ResolveInfo
import fe.linksheet.feature.app.ActivityAppInfo
import fe.linksheet.module.resolver.FilteredBrowserList
import fe.linksheet.database.entity.PreferredApp
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class AppSorter(
    private val queryAndAggregateUsageStats: (List<ResolveInfo>, Boolean) -> Map<String, Long>,
    private val toAppInfo: (ResolveInfo, Boolean) -> ActivityAppInfo,
    private val clock: Clock
) {
    data class Result(val sorted: List<ActivityAppInfo>, val filtered: ActivityAppInfo?)

    fun sort(
        appList: FilteredBrowserList,
        lastChosen: PreferredApp?,
        historyMap: Map<String, Long>,
        returnLastChosen: Boolean,
    ): Pair<List<ActivityAppInfo>, ActivityAppInfo?> {
        val apps = appList.apps.map { toAppInfo(it, false) }
        val filtered = if (returnLastChosen && lastChosen?.pkg != null) {
            apps.firstOrNull { it.packageName == lastChosen.pkg }
        } else null
        return apps to filtered
    }
}
