package fe.linksheet.module.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import java.lang.reflect.Type

object GlobalGsonContext {
    private var gson: Gson? = null
    
    fun configure(block: GsonBuilder.() -> Unit) {
        val builder = GsonBuilder()
        block(builder)
        gson = builder.create()
    }
    
    fun getGson(): Gson = gson ?: GsonBuilder().create()
    
    fun <T> registerTypeAdapter(type: Class<T>, typeAdapter: TypeAdapter<T>) {
        // Implementation for registering type adapters
    }
    
    fun <T> registerTypeAdapter(type: Type, typeAdapter: TypeAdapter<T>) {
        // Implementation for registering type adapters
    }
}
