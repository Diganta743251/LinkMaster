package fe.linksheet.module.repository

import fe.linksheet.module.database.dao.LibRedirectServiceStateDao
import fe.linksheet.module.database.entity.LibRedirectServiceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


class LibRedirectStateRepository(private val dao: LibRedirectServiceStateDao) {
    private fun getServiceState(serviceKey: String): Flow<LibRedirectServiceState?> {
        return dao.getServiceState(serviceKey)
    }

    fun isEnabledFlow(serviceKey: String): Flow<Boolean> {
        return getServiceState(serviceKey).map { it?.enabled ?: false }
    }

    suspend fun isEnabled(serviceKey: String) = isEnabledFlow(serviceKey).firstOrNull() == true

    suspend fun insert(state: LibRedirectServiceState) = dao.insert(state)
}
