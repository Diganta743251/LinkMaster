import fe.build.dependencies.Grrfe
import fe.build.dependencies._1fexd

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "fe.linksheet.lib.util"
    compileSdk = 36

    defaultConfig {
        minSdk = 25
    }

    packaging {
        resources {
            excludes += setOf("META-INF/{AL2.0,LGPL2.1}", "META-INF/atomicfu.kotlin_module", "META-INF/*.md")
        }
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
    implementation(platform(Grrfe.std.platform))
    // Use explicit coordinates to avoid stale buildSrc constants
    implementation(platform("com.github.1fexd.composekit:platform:_"))

    implementation(Grrfe.std.result.core)
    implementation("com.github.1fexd.composekit:core:_")
    implementation("com.github.1fexd.composekit:compose-core:_")

    implementation("androidx.core:core-ktx:_")
}
