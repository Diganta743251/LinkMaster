

plugins {
    id("com.android.library")
    kotlin("android")
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }

    packaging {
        resources {
            excludes += setOf("META-INF/{AL2.0,LGPL2.1}", "META-INF/atomicfu.kotlin_module", "META-INF/*.md")
        }
    }
}

dependencies {
    api(project(":test-core"))

    implementation(platform("androidx.compose:compose-bom:_"))
    implementation("androidx.compose.foundation:foundation:_")
    implementation("androidx.compose.ui:ui:_")
    implementation("androidx.compose.ui:ui-test-junit4:_")
    implementation("androidx.compose.ui:ui-test-manifest:_")

    implementation("androidx.test.ext:junit-ktx:_")
    api("androidx.test.uiautomator:uiautomator:_")
    implementation("androidx.activity:activity-ktx:_")
    implementation("androidx.activity:activity-compose:_")
    implementation("androidx.core:core-ktx:_")
}
