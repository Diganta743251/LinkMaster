package fe.buildlogic.extension

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DefaultConfig
import com.android.build.api.dsl.ProductFlavor
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import java.util.Properties

fun DefaultConfig.buildStringConfigField(name: String, value: String) {
    buildConfigField("String", name, "\"$value\"")
}

fun ProductFlavor.buildStringConfigField(name: String, value: String) {
    buildConfigField("String", name, "\"$value\"")
}

enum class CompilerOption(val option: String) {
    JVM_DEFAULT_ALL("-Xjvm-default=all"),
}

fun CommonExtension<*, *, *, *, *>.addCompilerOptions(vararg options: CompilerOption) {
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions>("kotlinOptions") {
        options.forEach { freeCompilerArgs += it.option }
    }
}

data class PluginOption(val id: String, val key: String, val value: String)

fun Project.addPluginOptions(vararg options: PluginOption) {
    val ksp = extensions.getByType<com.google.devtools.ksp.gradle.KspExtension>()
    options.forEach { ksp.arg(it.key, it.value) }
}

fun Project.getOrSystemEnv(name: String): String? {
    return System.getenv(name) ?: project.findProperty(name) as? String
}

fun Project.readPropertiesOrNull(path: String): Properties? {
    return rootProject.file(path).takeIf { it.exists() }?.inputStream()?.use { Properties().apply { load(it) } }
}