package fe.linksheet.util

import android.os.Debug
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

/**
 * Utility class for monitoring performance metrics
 */
object PerformanceMonitor {
    const val TAG = "PerformanceMonitor"
    private const val SLOW_OPERATION_THRESHOLD_MS = 1000L
    
    /**
     * Measure execution time of a suspend function
     */
    suspend inline fun <T> measureSuspend(
        operationName: String,
        block: suspend () -> T
    ): T {
        val startTime = System.currentTimeMillis()
        val startMemory = getMemoryUsage()
        
        return try {
            val result = block()
            val executionTime = System.currentTimeMillis() - startTime
            val memoryDelta = getMemoryUsage() - startMemory
            
            logPerformance(operationName, executionTime, memoryDelta)
            result
        } catch (e: Exception) {
            val executionTime = System.currentTimeMillis() - startTime
            Log.e(TAG, "$operationName failed after ${executionTime}ms", e)
            throw e
        }
    }
    
    /**
     * Measure execution time of a regular function
     */
    inline fun <T> measure(
        operationName: String,
        block: () -> T
    ): T {
        val startTime = System.currentTimeMillis()
        val startMemory = getMemoryUsage()
        
        return try {
            val result = block()
            val executionTime = System.currentTimeMillis() - startTime
            val memoryDelta = getMemoryUsage() - startMemory
            
            logPerformance(operationName, executionTime, memoryDelta)
            result
        } catch (e: Exception) {
            val executionTime = System.currentTimeMillis() - startTime
            Log.e(TAG, "$operationName failed after ${executionTime}ms", e)
            throw e
        }
    }
    
    /**
     * Get current memory usage in MB
     */
    fun getMemoryUsage(): Float {
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()
        return usedMemory / (1024f * 1024f)
    }
    
    /**
     * Get native heap size in MB
     */
    fun getNativeHeapSize(): Float {
        return Debug.getNativeHeapSize() / (1024f * 1024f)
    }
    
    /**
     * Log performance metrics
     */
    fun logPerformance(
        operationName: String,
        executionTimeMs: Long,
        memoryDeltaMb: Float
    ) {
        val logLevel = if (executionTimeMs > SLOW_OPERATION_THRESHOLD_MS) {
            Log.WARN
        } else {
            Log.DEBUG
        }
        
        val message = buildString {
            append("$operationName: ${executionTimeMs}ms")
            if (memoryDeltaMb > 0.1f) {
                append(", +${String.format("%.2f", memoryDeltaMb)}MB")
            }
        }
        
        Log.println(logLevel, TAG, message)
    }
    
    /**
     * Monitor memory usage continuously
     */
    fun startMemoryMonitoring(scope: CoroutineScope, intervalMs: Long = 5000L) {
        scope.launch(Dispatchers.IO) {
            while (true) {
                val memoryUsage = getMemoryUsage()
                val nativeHeap = getNativeHeapSize()
                
                if (memoryUsage > 100f) { // Log if using more than 100MB
                    Log.w(TAG, "High memory usage: ${String.format("%.2f", memoryUsage)}MB " +
                            "(native: ${String.format("%.2f", nativeHeap)}MB)")
                }
                
                kotlinx.coroutines.delay(intervalMs)
            }
        }
    }
    
    /**
     * Force garbage collection and log memory before/after
     */
    fun forceGC(reason: String = "Manual") {
        val beforeMemory = getMemoryUsage()
        System.gc()
        kotlinx.coroutines.runBlocking {
            kotlinx.coroutines.delay(100) // Give GC time to run
        }
        val afterMemory = getMemoryUsage()
        val freed = beforeMemory - afterMemory
        
        Log.d(TAG, "GC ($reason): ${String.format("%.2f", beforeMemory)}MB -> " +
                "${String.format("%.2f", afterMemory)}MB (freed: ${String.format("%.2f", freed)}MB)")
    }
}

/**
 * Extension function for easy performance measurement
 */
suspend inline fun <T> T.measurePerformance(
    operationName: String,
    block: suspend T.() -> Unit
) {
    PerformanceMonitor.measureSuspend(operationName) {
        this.block()
    }
}