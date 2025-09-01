
import fe.buildlogic.Version

plugins {
    id("com.android.library")
    kotlin("android")
    id("com.gitlab.grrfe.new-build-logic-plugin")
}

group = "fe.linksheet.testlib.ui"

android {
    namespace = group.toString()
    compileSdk = 36

    defaultConfig {
        minSdk = 25
    }

    buildFeatures {
        buildConfig = true
    }

    kotlin {
        jvmToolchain(21)
    }

    packaging {
        resources {
            excludes += setOf("META-INF/{AL2.0,LGPL2.1}", "META-INF/atomicfu.kotlin_module", "META-INF/*.md")
        }
    }
}

dependencies {
    api(project(":test-core"))

    implementation(platform("androidx.compose:compose-bom-alpha:_"))
    implementation(AndroidX.compose.foundation)
    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.ui.test)
    implementation(AndroidX.compose.ui.testJunit4)

    implementation(AndroidX.test.ext.junit.ktx)
    api(AndroidX.test.uiAutomator)
    implementation(AndroidX.activity.ktx)
    implementation(AndroidX.activity.compose)
    implementation(AndroidX.core.ktx)
}
