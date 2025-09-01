package fe.linksheet.module.database.dao

import androidx.room.Dao
import androidx.room.Query
import fe.linksheet.module.database.dao.base.BaseDao
import fe.linksheet.module.database.entity.AppSelection
import fe.linksheet.module.database.entity.AppSelectionHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface AppSelectionHistoryDao : BaseDao<AppSelectionHistory> {
    @Query("SELECT * FROM app_selection_history WHERE host = :host")
    fun getByHost(host: String): Flow<List<AppSelectionHistory>>

    @Query("SELECT packageName, MAX(lastUsed) as maxLastUsed FROM app_selection_history WHERE host = :host GROUP BY packageName")
    fun getLastUsedForHostGroupedByPackage(host: String): Flow<List<AppSelection>>

    @Query("DELETE FROM app_selection_history WHERE host = :host")
    suspend fun deleteByHost(host: String)

    @Query("DELETE FROM app_selection_history WHERE packageName = :packageName")
    suspend fun deleteByPackageName(packageName: String)

    @Query("DELETE FROM app_selection_history WHERE packageName IN (:packageNames)")
    suspend fun deleteByPackageNames(packageNames: List<String>)
    
    // Enhanced methods for history feature
    @Query("SELECT * FROM app_selection_history ORDER BY lastUsed DESC")
    fun getAllHistory(): Flow<List<AppSelectionHistory>>
    
    @Query("SELECT * FROM app_selection_history WHERE host LIKE '%' || :query || '%' OR packageName LIKE '%' || :query || '%' ORDER BY lastUsed DESC")
    fun getHistoryBySearch(query: String): Flow<List<AppSelectionHistory>>
    
    @Query("SELECT COUNT(*) FROM app_selection_history")
    suspend fun getHistoryCount(): Int
    
    @Query("DELETE FROM app_selection_history")
    suspend fun deleteAll()
    
    @Query("DELETE FROM app_selection_history WHERE id = :id")
    suspend fun deleteById(id: Int)
    
    @Query("DELETE FROM app_selection_history WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Int>)
    
    @Query("DELETE FROM app_selection_history WHERE lastUsed < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)
    
    @Query("DELETE FROM app_selection_history WHERE id NOT IN (SELECT id FROM app_selection_history ORDER BY lastUsed DESC LIMIT :limit)")
    suspend fun deleteExcessHistory(limit: Int)
    
    @Query("SELECT * FROM app_selection_history ORDER BY lastUsed DESC LIMIT :limit OFFSET :offset")
    fun getHistoryPaged(limit: Int, offset: Int): Flow<List<AppSelectionHistory>>
}
