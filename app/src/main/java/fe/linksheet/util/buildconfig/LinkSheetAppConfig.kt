package fe.linksheet.util.buildconfig

import fe.linksheet.config.AppConfig

object LinkSheetAppConfig : AppConfig {
    private val flavorConfig: FlavorConfig = FlavorConfig.Default

    override fun isPro(): Boolean {
        return flavorConfig.isPro
    }

    override fun showDonationBanner(): Boolean {
        return !isPro()
    }

    override fun supabaseHost(): String? = flavorConfig.supabaseHost
    override fun supabaseApiKey(): String? = flavorConfig.supabaseApiKey
}
