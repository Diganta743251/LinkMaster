package fe.linksheet.feature.wiki

import androidx.room.Dao
import androidx.room.Query
import fe.linksheet.common.dao.base.BaseDao
@Dao
interface WikiCacheDao : BaseDao<WikiCache> {
    @Query("SELECT * FROM wiki_cache WHERE url = :url ORDER BY timestamp DESC LIMIT 1")
    suspend fun getCachedText(url: String): WikiCache?
}
