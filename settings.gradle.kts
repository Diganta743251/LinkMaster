@file:Suppress("UnstableApiUsage")

rootProject.name = "LinkSheet"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.google.devtools.ksp" -> useVersion("2.2.20-1.0.29")
                // Gradle plugin portal marker id
                "com.google.devtools.ksp.gradle.plugin" -> useVersion("2.2.20-1.0.29")
            }
        }
    }

    plugins {
        id("de.fayard.refreshVersions") version "0.60.5"
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
        // Do not declare AGP versions here; AGP is already on the classpath via external build logic.
        // Kotlin and KSP plugin versions are resolved from the classpath to avoid
        // "already on the classpath with an unknown version" conflicts.
        // Explicitly set Kotlin versions to align with dependencies
        kotlin("android") version "2.2.20"
        kotlin("plugin.serialization") version "2.2.20"
        kotlin("plugin.compose") version "2.2.20"
        id("net.nemerosa.versioning")
    }
    // includeBuild("build-logic") // disabled: using standard plugins in modules
}
plugins {
    id("de.fayard.refreshVersions")
    id("org.gradle.toolchains.foojay-resolver-convention")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.mozilla.org/maven2")
        maven("https://s01.oss.sonatype.org/content/repositories/releases/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://storage.googleapis.com/r8-releases/raw")
    }
}

include(
    ":app",
    ":config",
    ":test-instrument",
    ":test-core",
    ":test-fake",
    ":scaffold",
    ":bottom-sheet",
    ":bottom-sheet-new",
    ":hidden-api",
    ":util",
    ":feature-app",
    ":feature-systeminfo"
)
project(":test-instrument").projectDir = file("test-lib/instrument")
project(":test-core").projectDir = file("test-lib/core")
project(":test-fake").projectDir = file("test-lib/fake")
project(":scaffold").projectDir = file("lib/scaffold")
project(":bottom-sheet").projectDir = file("lib/bottom-sheet")
project(":bottom-sheet-new").projectDir = file("lib/bottom-sheet-new")
project(":hidden-api").projectDir = file("lib/hidden-api")
project(":util").projectDir = file("lib/util")
project(":feature-app").projectDir = file("features/app")
project(":feature-systeminfo").projectDir = file("features/systeminfo")
 



