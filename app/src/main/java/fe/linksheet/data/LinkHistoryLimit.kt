package fe.linksheet.data

sealed class LinkHistoryLimit {
    object NO_LIMIT : LinkHistoryLimit()
    object ONE_HUNDRED : LinkHistoryLimit()
    object FIVE_HUNDRED : LinkHistoryLimit()
    object ONE_THOUSAND : LinkHistoryLimit()
    object FIVE_THOUSAND : LinkHistoryLimit()
    object TEN_THOUSAND : LinkHistoryLimit()
    data class CUSTOM_COUNT(val count: Int) : LinkHistoryLimit()
    
    companion object {
        val values = listOf(
            NO_LIMIT,
            ONE_HUNDRED,
            FIVE_HUNDRED,
            ONE_THOUSAND,
            FIVE_THOUSAND,
            TEN_THOUSAND
        )
    }
}
