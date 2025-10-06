package fe.linksheet.module.repository

import fe.linksheet.module.database.entity.PreferredApp

class PreferredAppRepository {
    suspend fun getByPackageName(packageName: String): List<PreferredApp> {
        // Minimal stub implementation: return empty list
        return emptyList()
    }
}
