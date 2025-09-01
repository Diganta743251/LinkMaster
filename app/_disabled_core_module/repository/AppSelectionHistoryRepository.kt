package fe.linksheet.module.repository

import android.net.Uri
import fe.linksheet.module.database.dao.AppSelectionHistoryDao
import fe.linksheet.module.database.entity.AppSelectionHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull


class AppSelectionHistoryRepository(private val dao: AppSelectionHistoryDao) {
    suspend fun insert(appSelectionHistory: AppSelectionHistory) = dao.insert(appSelectionHistory)
    suspend fun insert(appSelectionHistories: List<AppSelectionHistory>) = dao.insert(appSelectionHistories)

    suspend fun getLastUsedForHostGroupedByPackage(uri: Uri?): Map<String, Long>? {
        val host = uri?.host ?: return null
        val appSelections = dao.getLastUsedForHostGroupedByPackage(host).firstOrNull() ?: return null

        return appSelections.associate { it.packageName to it.maxLastUsed }
    }

    suspend fun delete(packageNames: List<String>) {
        dao.deleteByPackageNames(packageNames)
    }
    
    // Enhanced methods for history feature
    fun getAllHistory(): Flow<List<AppSelectionHistory>> = dao.getAllHistory()
    
    fun getHistoryBySearch(query: String): Flow<List<AppSelectionHistory>> = dao.getHistoryBySearch(query)
    
    suspend fun getHistoryCount(): Int = dao.getHistoryCount()
    
    suspend fun deleteAll() = dao.deleteAll()
    
    suspend fun deleteById(id: Int) = dao.deleteById(id)
    
    suspend fun deleteByIds(ids: List<Int>) = dao.deleteByIds(ids)
    
    suspend fun deleteOlderThan(timestamp: Long) = dao.deleteOlderThan(timestamp)
    
    suspend fun deleteExcessHistory(limit: Int) = dao.deleteExcessHistory(limit)
    
    fun getHistoryPaged(limit: Int, offset: Int): Flow<List<AppSelectionHistory>> = 
        dao.getHistoryPaged(limit, offset)
}
