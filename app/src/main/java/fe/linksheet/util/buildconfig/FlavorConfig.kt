package fe.linksheet.util.buildconfig

import androidx.annotation.Keep

@Keep
data class FlavorConfig(
    val isPro: Boolean,
    val supabaseHost: String,
    val supabaseApiKey: String,
) {
    companion object {
        val Default = FlavorConfig(false, "", "")

        // Minimal parser: returns Default for this build
        fun parseFlavorConfig(config: String?): FlavorConfig = Default
    }
}
