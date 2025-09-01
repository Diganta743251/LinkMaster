package fe.linksheet.module.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

val GlobalGsonModule = module {
    single<Gson> {
        GsonBuilder()
            .setPrettyPrinting()
            .create()
    }
}
