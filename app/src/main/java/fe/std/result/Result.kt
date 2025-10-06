package fe.std.result

sealed interface IResult<out T> {
    val exception: Throwable?
}

data class Success<T>(val value: T) : IResult<T> {
    override val exception: Throwable? = null
}

data class Failure<T>(override val exception: Throwable) : IResult<T>

val <T> T.success: IResult<T> get() = Success(this)

fun <T> IResult<T>.isSuccess(): Boolean = this is Success<T>
fun <T> IResult<T>.isFailure(): Boolean = this is Failure<T>

inline fun <T> tryCatch(block: () -> T): IResult<T> = try {
    Success(block())
} catch (t: Throwable) {
    Failure(t)
}

inline fun <T> IResult<T>.mapFailure(transform: (Throwable) -> Throwable): IResult<T> = when (this) {
    is Success -> this
    is Failure -> Failure(transform(this.exception))
}

fun <T> IResult<T>.getOrNull(): T? = (this as? Success<T>)?.value
