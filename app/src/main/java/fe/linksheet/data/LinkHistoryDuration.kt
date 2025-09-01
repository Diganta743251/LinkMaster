package fe.linksheet.data

sealed class LinkHistoryDuration {
    object DISABLED : LinkHistoryDuration()
    object ONE_DAY : LinkHistoryDuration()
    object SEVEN_DAYS : LinkHistoryDuration()
    object FOURTEEN_DAYS : LinkHistoryDuration()
    object THIRTY_DAYS : LinkHistoryDuration()
    data class CUSTOM_DAYS(val days: Int) : LinkHistoryDuration()
    
    companion object {
        val values = listOf(
            DISABLED,
            ONE_DAY,
            SEVEN_DAYS,
            FOURTEEN_DAYS,
            THIRTY_DAYS
        )
    }
}
