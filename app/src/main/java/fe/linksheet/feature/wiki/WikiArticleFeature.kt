package fe.linksheet.feature.wiki

import fe.linksheet.repository.WikiCacheRepository
import fe.linksheet.util.CacheResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WikiArticleFeature(
    val repository: WikiCacheRepository,
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    // Stub implementation - no backend connectivity needed
    private fun fetchText(url: String): String? {
        // Return null since we can't fetch from network
        return null
    }

    suspend fun getWikiText(url: String): String? = withContext(ioDispatcher) {
        val cacheResult = repository.getCached(url)
        if (cacheResult is CacheResult.Hit) {
            return@withContext cacheResult.value.text
        }

        // No network fetching - only return cached content
        return@withContext null
    }
}
