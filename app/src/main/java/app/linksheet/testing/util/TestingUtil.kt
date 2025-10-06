package app.linksheet.testing.util

import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.ComponentInfo
import android.content.pm.ResolveInfo
import fe.linksheet.feature.app.ActivityAppInfo
import app.linksheet.testing.fake.PackageInfoFake

val PackageInfoFake.packageName: String get() = this.pkgName

val PackageInfoFake.firstActivityResolveInfo: ResolveInfo?
    get() {
        val ai = ActivityInfo().apply {
            packageName = this@firstActivityResolveInfo.pkgName
            name = "MainActivity"
            applicationInfo = ApplicationInfo().apply { packageName = this@firstActivityResolveInfo.pkgName }
        }
        return ResolveInfo().apply { activityInfo = ai }
    }

fun listOfFirstActivityResolveInfo(vararg pkgs: PackageInfoFake): List<ResolveInfo> =
    pkgs.mapNotNull { it.firstActivityResolveInfo }

fun ResolveInfo.toActivityAppInfo(): ActivityAppInfo {
    val ci: ComponentInfo = this.activityInfo ?: ActivityInfo().apply { packageName = "" }
    val label = this.activityInfo?.packageName ?: ""
    return ActivityAppInfo(componentInfo = ci, labelStr = label, iconPainter = null)
}
