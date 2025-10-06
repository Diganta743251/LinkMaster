

plugins {
    id("com.android.library")
    kotlin("android")
    id("de.mannodermaus.android-junit5")
}

group = "fe.linksheet.testlib.fake"

android {
    namespace = group.toString()
    compileSdk = 36

    defaultConfig {
        minSdk = 25
    }

    buildFeatures {
        buildConfig = true
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
    api("androidx.test:runner:_")
    api("androidx.test:core-ktx:_")
    api("org.junit.jupiter:junit-jupiter-api:_")
    api("io.insert-koin:koin-test:_")

    implementation(platform("androidx.compose:compose-bom:_"))
    implementation("androidx.compose.ui:ui:_")
}
