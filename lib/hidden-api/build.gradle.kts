

plugins {
    id("com.android.library")
}
group = "fe.linksheet.hiddenapi"

android {
    namespace = group.toString()
    compileSdk = 36

    defaultConfig {
        minSdk = 25
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    annotationProcessor("dev.rikka.tools.refine:annotation-processor:_")
    compileOnly("dev.rikka.tools.refine:annotation:_")
    compileOnly("org.jetbrains:annotations:_")
    compileOnly("androidx.annotation:annotation:_")
}
