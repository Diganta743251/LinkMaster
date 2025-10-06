package fe.linksheet.util.buildconfig

import fe.linksheet.BuildConfig

data class BuildInfo(
    val versionName: String,
    val versionCode: Int,
    val builtAt: String,
    val flavor: String,
    val workflowId: String?,
)

object LinkSheetInfo {
    // Minimal BuildInfo without extra plugins; builtAt and workflowId left blank
    val buildInfo = BuildInfo(
        versionName = BuildConfig.VERSION_NAME,
        versionCode = BuildConfig.VERSION_CODE,
        builtAt = "",
        flavor = BuildConfig.BUILD_TYPE,
        workflowId = null
    )
}
