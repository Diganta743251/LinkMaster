
import fe.buildlogic.Version
import fe.buildlogic.common.CompilerOption
import fe.buildlogic.common.extension.addCompilerOptions

plugins {
    id("com.android.library")
    kotlin("android")
    id("com.gitlab.grrfe.new-build-logic-plugin")
    id("de.mannodermaus.android-junit5")
}

group = "fe.linksheet.testlib.core"

android {
    namespace = group.toString()
    compileSdk = 36

    defaultConfig {
        minSdk = 25
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    jvmToolchain(21)
    addCompilerOptions(CompilerOption.AllowKotlinPackage)
}

dependencies {
    api(AndroidX.test.runner)
    api(AndroidX.test.coreKtx)
    api(Testing.junit.jupiter.api)
    api(Koin.test)
}
