package fe.linksheet.module.database.migrations

import android.content.ComponentName
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration1to2 : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) = db.run {
        execSQL("ALTER TABLE openwith ADD packageName VARCHAR")

        val cursor = query("SELECT * FROM openwith")
        while(cursor.moveToNext()){
            val idIndex = cursor.getColumnIndex("_id")
            val componentIndex = cursor.getColumnIndex("component")

            if(componentIndex >= 0 && idIndex >= 0){
                val id = cursor.getInt(idIndex)
                val componentName = ComponentName.unflattenFromString(cursor.getString(componentIndex))!!
                execSQL("UPDATE openwith SET packageName = '${componentName.packageName}' WHERE _id = $id")
            }
        }
    }
}
