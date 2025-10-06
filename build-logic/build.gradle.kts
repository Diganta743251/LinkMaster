import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "fe.android.application"
            implementationClass = "fe.buildlogic.convention.AndroidApplicationConventionPlugin"
        }
        register("androidCompose") {
            id = "fe.android.compose"
            implementationClass = "fe.buildlogic.convention.AndroidComposeConventionPlugin"
        }
    }
}