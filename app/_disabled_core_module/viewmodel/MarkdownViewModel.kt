package fe.linksheet.module.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import fe.linksheet.feature.wiki.WikiArticleFeature
import fe.linksheet.feature.wiki.WikiCacheRepository

class MarkdownViewModel(
    val context: Application,
    val repository: WikiCacheRepository,
) : ViewModel() {
    // Stub implementation - no backend connectivity needed
    private val useCase = WikiArticleFeature(repository)
    suspend fun getWikiText(url: String): String? = useCase.getWikiText(url)
}
