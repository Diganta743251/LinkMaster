---
description: Repository Information Overview
alwaysApply: true
---

# LinkSheet Information

## Summary
LinkSheet is an Android application that reimplements the pre-Android 12 system link handling behavior, allowing users to choose which app to open links in. It provides features like setting preferred browsers, managing app preferences for specific hosts, and includes experimental integrations with ClearURLs, FastForward, and LibRedirect. The project follows a "nightly" rolling release model for rapid development.

## Structure
- **app**: Main application module
- **config**: Configuration module
- **features**: Feature modules (app, systeminfo)
- **lib**: Library modules (bottom-sheet, bottom-sheet-new, hidden-api, scaffold, util)
- **test-lib**: Testing libraries (core, fake, instrument)
- **.github**: GitHub workflows and actions
- **gradle**: Gradle wrapper and configuration
- **fastlane**: Metadata for app store listings

## Language & Runtime
**Language**: Kotlin
**Version**: 2.2.20-Beta1
**Build System**: Gradle 8.13
**Package Manager**: Gradle/Maven

## Dependencies
**Main Dependencies**:
- AndroidX Compose (UI framework)
- AndroidX Room (Database)
- OkHttp (Networking)
- Koin (Dependency Injection)
- Rikka Shizuku (System API access)
- Mozilla Components (Public suffix list)
- Coil (Image loading)

**Development Dependencies**:
- JUnit 5 (Testing)
- Robolectric (Android testing)
- Mockserver (API mocking)
- LeakCanary (Memory leak detection)

## Build & Installation
```bash
# Build debug APK
./gradlew assembleFossDebug

# Build release APK
./gradlew assembleFossRelease

# Build nightly APK
./gradlew assembleFossNightly

# Run tests
./gradlew testFossReleaseUnitTest
./gradlew connectedAndroidTest
```

## Testing
**Framework**: JUnit 5, AndroidX Test
**Test Location**: 
- Unit tests: `app/src/test`
- Instrumentation tests: `app/src/androidTest`
**Configuration**: 
- JUnit Platform configuration in build.gradle.kts
- Custom test runners for Android tests
**Run Command**:
```bash
./gradlew testFossReleaseUnitTest
./gradlew connectedAndroidTest
```

## Project Features
- **Product Flavors**: 
  - `foss`: Free and open-source version
  - `pro`: Premium version with additional features
- **Build Types**: 
  - `debug`: Development builds
  - `release`: Production builds
  - `nightly`: Automated nightly builds
  - `releaseDebug`: Release builds with debugging enabled
  - `migrate`: Special build for migration
  - `benchmark`: Performance testing build
- **CI/CD**: GitHub Actions workflows for automated testing and building
- **Localization**: Translations managed via Weblate
- **Signing**: Custom signing configurations for different build types

## Architecture
- **Module Structure**: Multi-module project with feature modules and library modules
- **UI Framework**: Jetpack Compose for modern UI development
- **Database**: Room for local data persistence
- **Dependency Injection**: Koin for service locator pattern
- **API Access**: Uses hidden API access via Shizuku for system integration