package fe.buildlogic.extension

import java.io.File
import java.util.*

fun File.readPropertiesOrNull(): Properties? {
    return if (exists()) inputStream().use { Properties().apply { load(it) } }
    else null
}
