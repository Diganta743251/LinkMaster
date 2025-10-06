

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.compose")
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

    implementation("androidx.compose.ui:ui-tooling-preview:_")
    implementation("androidx.compose.ui:ui:_")
    implementation("androidx.compose.material3:material3-android:_")
    implementation("androidx.compose.foundation:foundation:_")

    implementation("androidx.compose.material:material-icons-core:_")
    implementation("androidx.compose.material:material-icons-extended:_")
    implementation("androidx.activity:activity-compose:_")
    implementation("androidx.compose.material3:material3-android:_")
}
