package fe.linksheet.composable.ui

fun getLinkHistoryLimitText(count: Int): String {
    return when (count) {
        -1 -> "No limit"
        100 -> "100"
        500 -> "500"
        1000 -> "1,000"
        5000 -> "5,000"
        10000 -> "10,000"
        50000 -> "50,000"
        100000 -> "100,000"
        else -> count.toString()
    }
}
