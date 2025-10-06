package fe.linksheet.feature.wiki

<<<<<<< HEAD:app/src/main/java/fe/linksheet/feature/wiki/WikiArticleFeature.kt
import fe.linksheet.repository.WikiCacheRepository
=======
import fe.httpkt.Request
import fe.httpkt.ext.isHttpSuccess
import fe.httpkt.ext.readToString
>>>>>>> 77b99c2077b8dfa56f994c5d1087e74867e7da51:features/wiki/src/main/kotlin/fe/linksheet/feature/wiki/WikiArticleFeature.kt
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
