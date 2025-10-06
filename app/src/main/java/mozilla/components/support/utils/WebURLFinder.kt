package mozilla.components.support.utils

class WebURLFinder(private val text: String) {
    fun bestWebURL(): String? {
        val regex = Regex("(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)")
        return regex.find(text)?.value
    }
}
