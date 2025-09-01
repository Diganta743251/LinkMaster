package fe.linksheet.log

import android.util.Log

object Logger {
    fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }
    
    fun info(tag: String, message: String) {
        Log.i(tag, message)
    }
    
    fun warning(tag: String, message: String) {
        Log.w(tag, message)
    }
    
    fun error(tag: String, message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }
}
