import java.io.File

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.compose")
    id("kotlin-parcelize")
}

dependencies {
<<<<<<< HEAD
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.13.1")
=======
    implementation(project(":feature-systeminfo"))
    implementation(project(":feature-app"))
    implementation(project(":feature-wiki"))
>>>>>>> 77b99c2077b8dfa56f994c5d1087e74867e7da51

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
<<<<<<< HEAD
    implementation(project(":feature-app"))
    implementation(project(":feature-systeminfo"))
    // ComposeKit routing API used by Route.kt
    implementation("com.github.1fexd.composekit:compose-route:_")
    debugImplementation(project(":test-fake"))
=======
    implementation(project(":common"))
>>>>>>> 77b99c2077b8dfa56f994c5d1087e74867e7da51

    // ZXing for QR code generation
    implementation("com.google.zxing:core:_")

    // OkHttp for MediaType extensions
    implementation("com.squareup.okhttp3:okhttp:_")

    // Mozilla Components used by IntentParser
    implementation("org.mozilla.components:support-utils:_")
    implementation("org.mozilla.components:lib-publicsuffixlist:_")

<<<<<<< HEAD
    // Grrfe kotlin-ext used by util and app sources (result/uri, etc.)
    implementation(platform("com.gitlab.grrfe.kotlin-ext:platform:_"))
    implementation("com.gitlab.grrfe.kotlin-ext:core")
    implementation("com.gitlab.grrfe.kotlin-ext:result-core")
    implementation("com.gitlab.grrfe.kotlin-ext:uri")
=======
    implementation(AndroidX.compose.material.icons.core)
    implementation(AndroidX.compose.material.icons.extended)
    implementation(AndroidX.activity.compose)
    implementation("sh.calvin.reorderable:reorderable:_")

    implementation(AndroidX.core.ktx)
    implementation(AndroidX.compose.animation)
    implementation(AndroidX.navigation.compose)

    implementation(AndroidX.lifecycle.process)
    implementation(AndroidX.lifecycle.runtime.compose)
    implementation(AndroidX.lifecycle.viewModelCompose)
    implementation(AndroidX.lifecycle.runtime.ktx)
    implementation(AndroidX.startup.runtime)

    implementation(AndroidX.webkit)
    implementation(AndroidX.browser)
    implementation(AndroidX.work.runtimeKtx)
    testImplementation(AndroidX.work.testing)

    implementation(AndroidX.room.runtime)
    implementation(AndroidX.room.ktx)
    ksp(AndroidX.room.compiler)

    implementation(Google.android.material)
    implementation(Google.accompanist.permissions)

    implementation(Koin.android)
    implementation(Koin.compose)
    implementation(Koin.workManager)
    implementation("org.jetbrains.kotlin:kotlin-reflect:_")

    implementation("io.coil-kt.coil3:coil-compose:_")
    implementation("io.coil-kt.coil3:coil-core:_")
    implementation("io.coil-kt.coil3:coil-compose:_")
    implementation("io.coil-kt.coil3:coil-network-okhttp:_")
    implementation("io.coil-kt.coil3:coil-network-okhttp:_")
    implementation("io.coil-kt.coil3:coil-network-ktor3:_")
    implementation("io.coil-kt.coil3:coil-test:_")

    implementation("com.github.seancfoley:ipaddress:_")
    implementation("io.github.fornewid:placeholder-material3:_")
    implementation("io.viascom.nanoid:nanoid:_")

    implementation(platform(LinkSheet.flavors.bom))
//    implementation(LinkSheet.flavors.core)
    implementation("com.github.LinkSheet.flavors:core:_")

    implementation(LinkSheet.interconnect)

    implementation(JetBrains.ktor.client.core)
    implementation(JetBrains.ktor.client.gson)
    implementation(JetBrains.ktor.client.okHttp)
    implementation(JetBrains.ktor.client.android)
    implementation(JetBrains.ktor.client.logging)
    implementation(JetBrains.ktor.client.contentNegotiation)
    implementation(JetBrains.ktor.client.json)
    implementation(JetBrains.ktor.client.encoding)
    implementation(JetBrains.ktor.plugins.serialization.gson)
    implementation("io.ktor:ktor-client-okhttp-jvm:_")
    testImplementation(JetBrains.ktor.client.mock)

    implementation(platform(Grrfe.std.bom))
    androidTestImplementation(platform(Grrfe.std.bom))
    implementation(Grrfe.std.core)
    implementation(Grrfe.std.time.core)
    implementation(Grrfe.std.time.java)
    implementation(Grrfe.std.result.core)
    implementation(Grrfe.std.uri)
    implementation(Grrfe.std.stringbuilder)
    implementation(Grrfe.std.test)
    implementation(Grrfe.std.process.core)

    implementation(platform(Grrfe.httpkt.bom))
    implementation(Grrfe.httpkt.core)
    implementation(Grrfe.httpkt.serialization.gson)

    implementation(platform(Grrfe.gsonExt.bom))
    implementation(Grrfe.gsonExt.core)
    implementation(Grrfe.gsonExt.koin)

    implementation(_1fexd.clearUrl)
    implementation(Grrfe.signify)
    implementation(_1fexd.fastForward)
>>>>>>> 77b99c2077b8dfa56f994c5d1087e74867e7da51
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