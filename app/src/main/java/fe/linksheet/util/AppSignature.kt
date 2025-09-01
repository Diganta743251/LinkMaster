package fe.linksheet.util

import android.content.Context
import android.content.pm.getSignature
import androidx.annotation.StringRes
import fe.linksheet.BuildConfig
import fe.linksheet.R
// Temporarily disabled flavors imports due to Kotlin compatibility issues
// import fe.linksheet.lib.flavors.LinkSheetApp
// import fe.linksheet.lib.flavors.Signature

object AppSignature {
    private lateinit var buildType: SignatureBuildType

    enum class SignatureBuildType(@StringRes val stringRes: Int) {
        Manual(R.string.manual_build),
        CI(R.string.github_pipeline_build),
        Unofficial(R.string.built_by_error),
        Debug(R.string.debug_build)
    }

    fun checkSignature(context: Context): SignatureBuildType {
        if (BuildConfig.DEBUG) return SignatureBuildType.Debug
        if (this::buildType.isInitialized) return buildType

        // Temporarily simplified signature checking
        buildType = SignatureBuildType.Manual
        return buildType
    }
}
