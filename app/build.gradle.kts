import java.io.File

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.compose")
    id("kotlin-parcelize")
}

dependencies {
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.13.1")

    // Compose
    implementation(platform("androidx.compose:compose-bom:_"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3-android:_")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.activity:activity-compose:_")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:_")
    implementation("androidx.navigation:navigation-compose:_")
    implementation("androidx.navigation:navigation-runtime-ktx:_")
    // Icons
    implementation("androidx.compose.material:material-icons-extended:_")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Koin DI
    implementation("io.insert-koin:koin-core:_")
    implementation("io.insert-koin:koin-android:_")
    implementation("io.insert-koin:koin-androidx-compose:_")

    // LinkSheet interconnect (AIDL/service interfaces)
    implementation("com.github.LinkSheet:interconnect:_")

    // Coil v3
    implementation("io.coil-kt.coil3:coil:_")
    implementation("io.coil-kt.coil3:coil-compose:_")
    debugImplementation("io.coil-kt.coil3:coil-test:_")

    // AndroidX Browser for CustomTabsService
    implementation("androidx.browser:browser:_")

    // AndroidX SQLite for SupportSQLiteOpenHelper
    implementation("androidx.sqlite:sqlite:_")
    implementation("androidx.sqlite:sqlite-framework:_")

    // ComposeKit platform and prefs used by app sources
    implementation(platform("com.github.1fexd.composekit:platform:_"))
    implementation("com.github.1fexd.composekit:core")
    implementation("com.github.1fexd.composekit:compose-core")
    implementation("com.github.1fexd.composekit:preference-core")
    implementation("com.github.1fexd.composekit:preference-compose-core")
    // Additional ComposeKit modules used across app code
    implementation("com.github.1fexd.composekit:compose-layout")
    implementation("com.github.1fexd.composekit:compose-component")
    implementation("com.github.1fexd.composekit:compose-app")
    implementation("com.github.1fexd.composekit:compose-dialog")
    implementation("com.github.1fexd.composekit:span-core")
    implementation("com.github.1fexd.composekit:span-compose")

    // Placeholder shimmer
    implementation("io.github.fornewid:placeholder-material3:_")
    // gson for GlobalGsonModule/Context
    implementation("com.google.code.gson:gson:_")
    implementation(project(":bottom-sheet"))
    implementation(project(":config"))
    implementation(project(":util"))
    implementation(project(":feature-app"))
    implementation(project(":feature-systeminfo"))
    // ComposeKit routing API used by Route.kt
    implementation("com.github.1fexd.composekit:compose-route:_")
    debugImplementation(project(":test-fake"))

    // ZXing for QR code generation
    implementation("com.google.zxing:core:_")

    // OkHttp for MediaType extensions
    implementation("com.squareup.okhttp3:okhttp:_")

    // Mozilla Components used by IntentParser
    implementation("org.mozilla.components:support-utils:_")
    implementation("org.mozilla.components:lib-publicsuffixlist:_")

    // Grrfe kotlin-ext used by util and app sources (result/uri, etc.)
    implementation(platform("com.gitlab.grrfe.kotlin-ext:platform:_"))
    implementation("com.gitlab.grrfe.kotlin-ext:core")
    implementation("com.gitlab.grrfe.kotlin-ext:result-core")
    implementation("com.gitlab.grrfe.kotlin-ext:uri")
    implementation("com.github.1fexd.libredirectkt:lib:_")
}
android {
    namespace = "fe.linksheet"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }

    // Note: Source exclusions removed to fix build.
    // The app will compile with all sources. 
    // Missing dependencies will need to be resolved or stubbed.
}