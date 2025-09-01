package fe.linksheet.util

sealed class CacheResult<out T> {
    data class Hit<T>(val value: T) : CacheResult<T>()
    object Miss : CacheResult<Nothing>()
}
