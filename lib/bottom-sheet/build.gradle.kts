

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.compose")
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
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:_"))
    implementation("androidx.compose.ui:ui:_")
    implementation("androidx.compose.material3:material3-android:_")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:_")
}
