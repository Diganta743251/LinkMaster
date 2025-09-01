# Play Store Compliance Changes

This document outlines all the changes made to ensure LinkSheet complies with Google Play Store policies for link handling applications.

## Removed Features and Modules

### 1. Network and Backend Connectivity
- **HTTP Module**: Removed all HTTP client functionality
- **Remote Config**: Removed remote configuration fetching
- **URL Resolver**: Removed external URL resolution services
- **Analytics**: Removed all analytics and telemetry
- **Paste Service**: Removed clipboard URL processing with external services

### 2. Experimental and Development Features
- **Experiments Module**: Removed experimental features system
- **Debug Module**: Removed debug tools and developer options
- **Nightly Features**: Removed nightly-specific experimental features
- **Version Tracking**: Removed version tracking with analytics

### 3. Background Processing
- **Work Manager**: Removed background work scheduling
- **Statistics Service**: Removed usage time tracking
- **Remote Asset Fetcher**: Removed background asset downloading

### 4. External URL Processing
- **ClearURL**: Removed URL cleaning functionality
- **FastForward**: Removed redirect bypassing
- **LibRedirect**: Removed alternative service redirects
- **AMP2HTML**: Removed AMP processing
- **Embed Resolve**: Removed embed URL resolution

### 5. System Access and Hidden APIs
- **Shizuku Integration**: Removed system-level API access
- **Hidden API Bypass**: Removed hidden Android API usage

### 6. Advertising and Monetization
- **AdMob**: Removed Google Ads integration
- **Firebase Analytics**: Removed analytics tracking

### 7. Downloader Features
- **File Downloader**: Removed external file downloading capability
- **Download Settings**: Removed download configuration UI

## Modified Files

### Core Application
- `LinkSheetApp.kt`: Removed module registrations for deleted features
- `build.gradle.kts`: Removed dependencies for network, analytics, and experimental features

### Navigation
- `Route.kt`: Removed routes for deleted features
- `AdvancedSettingsRoute.kt`: Removed experiments navigation

### Removed Directories
- `/module/analytics/`
- `/module/downloader/`
- `/module/experiment/`
- `/module/http/`
- `/module/paste/`
- `/module/remoteconfig/`
- `/module/shizuku/`
- `/module/statistic/`
- `/module/versiontracker/`
- `/module/workmanager/`
- `/activity/bottomsheet/content/failure/`
- `/composable/page/home/card/news/`
- `/composable/page/settings/advanced/experiments/`
- `/composable/page/settings/link/downloader/`

## Benefits of Changes

### 1. Play Store Compliance
- ✅ No unauthorized network access
- ✅ No system-level API abuse
- ✅ No experimental features that could be flagged
- ✅ No hidden functionality
- ✅ No advertising in link handling context

### 2. App Performance
- 🚀 Reduced APK size by removing unused modules
- 🚀 Faster startup time without background services
- 🚀 Lower memory usage without analytics and tracking
- 🚀 Cleaner UI without experimental options

### 3. User Privacy
- 🔒 No data collection or analytics
- 🔒 No external network requests
- 🔒 No usage tracking
- 🔒 No background processing

### 4. Maintenance
- 🛠️ Simpler codebase without experimental features
- 🛠️ Fewer dependencies to maintain
- 🛠️ Reduced complexity in build configuration

## Remaining Core Features

The following core features remain intact and compliant:

- ✅ Link interception and app selection
- ✅ Browser preferences management
- ✅ App-specific link handling rules
- ✅ Custom browser integration
- ✅ Local settings and preferences
- ✅ Theme and UI customization
- ✅ Import/Export of settings (local only)

## Build Configuration Changes

### Removed Dependencies
- OkHttp and networking libraries
- Firebase and analytics SDKs
- Work Manager
- AdMob
- External URL processing libraries
- Shizuku system access libraries

### Maintained Dependencies
- AndroidX Compose UI framework
- Room database for local storage
- Koin dependency injection
- Core Android libraries

This streamlined version focuses solely on the core link handling functionality while ensuring full compliance with Google Play Store policies.