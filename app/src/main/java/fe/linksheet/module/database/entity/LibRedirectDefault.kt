package fe.linksheet.module.database.entity

data class LibRedirectDefault(
    val serviceKey: String,
    val frontendKey: String,
    val instanceUrl: String,
) {
    companion object {
        const val randomInstance = "RANDOM_INSTANCE"
        const val IgnoreIntentKey = "IGNORE_LIBREDIRECT"
    }
}
