package fe.linksheet.module.database

import android.content.Context
import fe.linksheet.module.log.Logger
import org.koin.dsl.module

val DatabaseModule = module {
    single<LinkSheetDatabase> {
        LinkSheetDatabase.create(context = get(), logger = get(), name = "linksheet")
    }
}

// Minimal stub database
class LinkSheetDatabase {
    companion object {
        fun create(context: Context, logger: Logger, name: String): LinkSheetDatabase {
            return LinkSheetDatabase()
        }
    }
}
