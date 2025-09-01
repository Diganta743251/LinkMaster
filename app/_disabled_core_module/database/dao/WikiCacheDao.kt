package fe.linksheet.module.database.dao

import androidx.room.Dao
import androidx.room.Query
import fe.linksheet.module.database.dao.base.BaseDao
import fe.linksheet.module.database.entity.WikiCache

@Dao
interface WikiCacheDao : BaseDao<WikiCache> {
    @Query("SELECT * FROM wiki_cache WHERE url = :url ORDER BY timestamp DESC LIMIT 1")
    suspend fun getCachedText(url: String): WikiCache?
}
