import fe.build.dependencies.Grrfe
import fe.build.dependencies._1fexd

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
}


android {
    namespace = "fe.linksheet.feature.app"
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
    implementation(project(":util"))
    implementation(project(":test-fake"))
    implementation("androidx.compose.ui:ui-graphics:_")
    testImplementation("org.robolectric:robolectric:_")
    compileOnly(project(":hidden-api"))

    implementation(platform(Grrfe.std.platform))
    implementation(Grrfe.std.core)
    implementation(Grrfe.std.result.core)

    implementation(platform(_1fexd.composeKit.platform))
    implementation(_1fexd.composeKit.core)
    implementation(_1fexd.composeKit.process)
    implementation(_1fexd.composeKit.compose.core)

    implementation(platform(Grrfe.gsonExt.platform))
    implementation(Grrfe.gsonExt.core)

    implementation("androidx.core:core-ktx:_")

    testImplementation("androidx.test.ext:junit-ktx:_")
    testImplementation(project(":test-core"))
    testImplementation(Grrfe.std.test)
    testImplementation(Grrfe.std.result.assert)
    testImplementation("com.willowtreeapps.assertk:assertk:_")
}
