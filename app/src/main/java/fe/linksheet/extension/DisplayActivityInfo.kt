package fe.linksheet.extension

data class DisplayActivityInfo(
    val packageName: String,
    val activityName: String
) {
    companion object {
        val labelComparator = compareBy<DisplayActivityInfo> { it.packageName }
    }
}
