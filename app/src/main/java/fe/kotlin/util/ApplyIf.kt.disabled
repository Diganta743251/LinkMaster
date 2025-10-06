package fe.kotlin.util

inline fun <T> T.applyIf(condition: Boolean, block: T.() -> T): T {
    return if (condition) this.block() else this
}
