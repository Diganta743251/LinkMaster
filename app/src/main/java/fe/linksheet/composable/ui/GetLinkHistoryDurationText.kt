package fe.linksheet.composable.ui

fun getLinkHistoryDurationText(days: Int): String {
    return when (days) {
        0 -> "Never"
        1 -> "1 day"
        3 -> "3 days"
        7 -> "1 week"
        14 -> "2 weeks"
        30 -> "1 month"
        90 -> "3 months"
        180 -> "6 months"
        365 -> "1 year"
        -1 -> "Forever"
        else -> "$days days"
    }
}
