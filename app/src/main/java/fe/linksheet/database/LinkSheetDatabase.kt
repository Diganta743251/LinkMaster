package fe.linksheet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fe.linksheet.database.entity.AppSelectionHistory
import fe.linksheet.database.entity.PreferredApp
import fe.linksheet.database.entity.DisableInAppBrowserInSelected
import fe.linksheet.database.entity.WikiCache
import fe.linksheet.database.entity.LibRedirectDefault

@Database(
    entities = [
        AppSelectionHistory::class,
        PreferredApp::class,
        DisableInAppBrowserInSelected::class,
        WikiCache::class,
        LibRedirectDefault::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LinkSheetDatabase : RoomDatabase() {
    // Database implementation will be added later
}

