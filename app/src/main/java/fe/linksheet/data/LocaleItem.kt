package fe.linksheet.data

data class LocaleItem(
    val locale: String,
    val displayName: String,
    val isDeviceLanguage: Boolean = false
) {
    fun update(locale: String): LocaleItem {
        return copy(locale = locale)
    }
}

data class DisplayLocaleItem(
    val item: LocaleItem,
    val isSelected: Boolean = false
)
