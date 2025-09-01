
import fe.buildlogic.Version

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.compose")
    id("com.gitlab.grrfe.new-build-logic-plugin")
}

group = "fe.linksheet.bottom.sheet"

android {
    namespace = group.toString()
    compileSdk = 36

    defaultConfig {
        minSdk = 25
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(platform("androidx.compose:compose-bom-alpha:_"))
    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.Lifecycle.viewModelCompose)
}
