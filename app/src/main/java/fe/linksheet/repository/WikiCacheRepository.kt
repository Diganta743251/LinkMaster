package fe.linksheet.repository

import fe.linksheet.database.entity.WikiCache
import fe.linksheet.util.CacheResult

class WikiCacheRepository {
    fun getCached(url: String): CacheResult<WikiCache> {
        // Stub implementation - no backend connectivity
        return CacheResult.Miss
    }
}
