package fe.android.preference.helper

abstract class OptionTypeMapper<T, K>(
    val key: (T) -> K,
    val options: () -> Array<T>
)
