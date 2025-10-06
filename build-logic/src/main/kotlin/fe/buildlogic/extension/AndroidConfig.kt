package fe.buildlogic.extension

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion

fun configureAndroid(ext: ApplicationExtension) {
    ext.apply {
        // Set reasonable defaults; module build.gradle can override
        compileSdk = 34
        defaultConfig {
            minSdk = 21
            targetSdk = 34
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
            isCoreLibraryDesugaringEnabled = false
        }
        packaging {
            resources {
                excludes += setOf("META-INF/{AL2.0,LGPL2.1}")
            }
        }
    }
}

fun configureCompose(ext: ApplicationExtension) {
    ext.apply {
        buildFeatures.compose = true
    }
}
