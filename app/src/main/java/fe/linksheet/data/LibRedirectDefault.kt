package fe.linksheet.data

object LibRedirectDefault {
    val INSTANCE_URL = "https://libreddit.kavin.rocks"
    val SERVICES = listOf("reddit", "twitter", "youtube", "instagram", "tiktok")
    val FRONTENDS = mapOf(
        "reddit" to "libreddit",
        "twitter" to "nitter",
        "youtube" to "piped",
        "instagram" to "bibliogram",
        "tiktok" to "proxitok"
    )
}
