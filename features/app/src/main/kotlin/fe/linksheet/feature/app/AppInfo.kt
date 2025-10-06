package fe.linksheet.feature.app

import android.content.pm.ComponentInfo
import android.os.Parcelable
import fe.android.compose.icon.IconPainter
import fe.kotlin.util.applyIf
import fe.linksheet.util.extension.android.componentName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

enum class LinkHandling {
    Browser,
    Allowed,
    Disallowed,
    Unsupported
}

@Parcelize
class DomainVerificationAppInfo(
    val pkgName: String,
    val labelStr: String,
    @IgnoredOnParcel val iconPainter: IconPainter? = null,
    val flags: Int,
    val installTime: Long? = null,
    val linkHandling: LinkHandling,
    val stateNone: MutableList<String>,
    val stateSelected: MutableList<String>,
    val stateVerified: MutableList<String>,
) : AppInfo(pkgName, labelStr, iconPainter) {

    @IgnoredOnParcel
    val enabled by lazy {
        linkHandling == LinkHandling.Allowed && (stateVerified.isNotEmpty() || stateSelected.isNotEmpty())
    }

    @IgnoredOnParcel
    val hostSum by lazy {
        stateNone.size + stateSelected.size + stateVerified.size
    }

    @IgnoredOnParcel
    val hostSet by lazy {
        (stateNone + stateSelected + stateVerified).toSet()
    }
}

typealias ActivityAppInfoStatus = Pair<ActivityAppInfo, Boolean>



@Parcelize
open class ActivityAppInfo(
    val componentInfo: @RawValue ComponentInfo,
    val labelStr: String,
    @IgnoredOnParcel val iconPainter: IconPainter? = null,
) : AppInfo(componentInfo.packageName, labelStr, iconPainter) {

    @IgnoredOnParcel
    val componentName by lazy { componentInfo.componentName }

    @IgnoredOnParcel
    val flatComponentName by lazy { componentName.flattenToString() }

    companion object {
        val labelComparator = compareBy<ActivityAppInfo> { it.compareLabel }
    }
}



@Parcelize
open class AppInfo(
    val packageName: String,
    val label: String,
    @IgnoredOnParcel val icon: IconPainter? = null,
) : Parcelable {

    @IgnoredOnParcel
    val compareLabel = label.lowercase()

    fun matches(query: String): Boolean {
        return compareLabel.contains(query, ignoreCase = true) || packageName.contains(
            query,
            ignoreCase = true
        )
    }

    companion object {
        val labelComparator = compareBy<AppInfo> { it.compareLabel }
    }
}

fun <T : AppInfo> List<T>.labelSorted(sorted: Boolean = true): List<T> {
    return if (sorted) this.sortedWith(AppInfo.labelComparator) else this
}

