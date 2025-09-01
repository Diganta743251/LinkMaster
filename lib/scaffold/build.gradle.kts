
import fe.buildlogic.Version

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.compose")
    id("com.gitlab.grrfe.new-build-logic-plugin")
}

group = "fe.linksheet.scaffold"

android {
    namespace = group.toString()
    compileSdk = 36

    defaultConfig {
        minSdk = 25
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(platform("androidx.compose:compose-bom-alpha:_"))

    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.material3)
    implementation(AndroidX.compose.foundation)

    implementation(AndroidX.compose.material.icons.core)
    implementation(AndroidX.compose.material.icons.extended)
    implementation(AndroidX.activity.compose)
    implementation("androidx.compose.material3:material3-android:_")
}
