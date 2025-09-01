package fe.linksheet.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "disable_in_app_browser_in_selected", indices = [Index("packageName", unique = true)])
data class DisableInAppBrowserInSelected(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Int = 0,
    val packageName: String
)
