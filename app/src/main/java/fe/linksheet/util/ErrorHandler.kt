package fe.linksheet.util

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

/**
 * Centralized error handling utility
 */
object ErrorHandler {
    private const val TAG = "ErrorHandler"
    
    /**
     * Handle different types of errors and return user-friendly messages
     */
    fun getErrorMessage(context: Context, throwable: Throwable): String {
        Log.e(TAG, "Error occurred", throwable)
        
        return when (throwable) {
            is UnknownHostException -> "No internet connection available"
            is IOException -> "Network error occurred. Please try again."
            is SecurityException -> "Permission denied. Please check app permissions."
            is OutOfMemoryError -> "Not enough memory available. Try closing other apps."
            is IllegalArgumentException -> "Invalid input provided"
            is IllegalStateException -> "App is in an invalid state. Please restart."
            else -> throwable.message ?: "An unexpected error occurred"
        }
    }
    
    /**
     * Handle errors with automatic retry logic
     */
    suspend fun <T> handleWithRetry(
        maxRetries: Int = 3,
        delayMs: Long = 1000,
        operation: suspend () -> T
    ): Result<T> {
        repeat(maxRetries) { attempt ->
            try {
                val result = operation()
                return Result.success(result)
            } catch (e: Exception) {
                Log.w(TAG, "Attempt ${attempt + 1} failed", e)
                if (attempt == maxRetries - 1) {
                    return Result.failure(e)
                }
                kotlinx.coroutines.delay(delayMs * (attempt + 1)) // Exponential backoff
            }
        }
        return Result.failure(IllegalStateException("Max retries exceeded"))
    }
    
    /**
     * Show error in snackbar with optional retry action
     */
    fun showError(
        scope: CoroutineScope,
        snackbarHostState: SnackbarHostState,
        context: Context,
        throwable: Throwable,
        onRetry: (() -> Unit)? = null
    ) {
        val message = getErrorMessage(context, throwable)
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = if (onRetry != null) "Retry" else null,
                withDismissAction = true
            )
            
            if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                onRetry?.invoke()
            }
        }
    }
}

/**
 * Composable for handling errors with snackbar
 */
@Composable
fun ErrorSnackbarHandler(
    error: Throwable?,
    onErrorShown: () -> Unit,
    onRetry: (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): SnackbarHostState {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    
    LaunchedEffect(error) {
        error?.let {
            ErrorHandler.showError(
                scope = scope,
                snackbarHostState = snackbarHostState,
                context = context,
                throwable = it,
                onRetry = onRetry
            )
            onErrorShown()
        }
    }
    
    return snackbarHostState
}

/**
 * Extension function for safe execution with error handling
 */
suspend inline fun <T> safeExecute(
    crossinline operation: suspend () -> T,
    crossinline onError: (Throwable) -> Unit = {}
): T? {
    return try {
        operation()
    } catch (e: Exception) {
        Log.e("SafeExecute", "Operation failed", e)
        onError(e)
        null
    }
}

/**
 * Extension function for Result type error handling
 */
inline fun <T> Result<T>.onFailureLog(tag: String = "Result"): Result<T> {
    onFailure { throwable ->
        Log.e(tag, "Operation failed", throwable)
    }
    return this
}

/**
 * Data class for error state
 */
data class ErrorState(
    val throwable: Throwable? = null,
    val message: String? = null,
    val canRetry: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
) {
    val hasError: Boolean get() = throwable != null || message != null
    
    companion object {
        fun none() = ErrorState()
        fun from(throwable: Throwable, canRetry: Boolean = true) = 
            ErrorState(throwable = throwable, canRetry = canRetry)
        fun from(message: String, canRetry: Boolean = true) = 
            ErrorState(message = message, canRetry = canRetry)
    }
}