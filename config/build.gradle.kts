import fe.buildlogic.Version

plugins {
    id("com.android.library")
    kotlin("android")
    id("com.gitlab.grrfe.new-build-logic-plugin")
}

group = "fe.linksheet.config"

android {
    namespace = group.toString()
    compileSdk = 36

    defaultConfig {
        minSdk = 25
    }
}

kotlin {
    jvmToolchain(21)
}
