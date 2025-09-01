# LinkMaster - Smart Link Organizer by Diganta

<div align="center">
  <img src="app/src/main/res/drawable/ic_launcher_foreground_vector.xml" alt="LinkMaster Logo" width="120" height="120">
  
  **Privacy-First Link Management for Android**
  
  [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE_LINKMASTER)
  [![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
  [![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
</div>

## 🚀 About LinkMaster

LinkMaster is a powerful, privacy-first link management app for Android that helps you organize, analyze, and share your links efficiently. Built with modern Android development practices, Material You design, and optimized for Google Play Store distribution with AdMob integration and Google Play Billing support.

### ✨ Key Features

#### 🆓 Free Features
- **Smart Link Handling** - Intercept and manage links from any app
- **Link Analytics** - Track clicks, sources, and usage patterns (stored locally)
- **QR Code Generation** - Create beautiful QR codes with custom colors
- **Basic Folders** - Organize links into categories
- **Theme Selection** - Light, Dark, and Neon themes
- **Privacy First** - All data stored locally by default

#### 💎 LinkMaster Pro Features
- **Cloud Sync** - Sync across all your devices with Firebase
- **Advanced Analytics** - Detailed insights and reports
- **Unlimited Folders** - Create as many folders as you need
- **Password Protection** - Secure your sensitive links
- **Custom Domains** - Use your own domain for short links
- **Ad-Free Experience** - No advertisements
- **Priority Support** - Get help when you need it

## 📱 Screenshots

| Light Theme | Dark Theme | Neon Theme |
|-------------|------------|------------|
| ![Light](screenshots/light.png) | ![Dark](screenshots/dark.png) | ![Neon](screenshots/neon.png) |

## 🛠 Technical Stack

- **Language**: Kotlin 100%
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Repository pattern
- **Database**: Room (SQLite)
- **Dependency Injection**: Koin
- **Networking**: OkHttp + Ktor
- **Cloud Backend**: Firebase (Auth + Firestore)
- **Monetization**: Google Play Billing + AdMob
- **QR Codes**: ZXing
- **Encryption**: AES-256-GCM

## 🏗 Building from Source

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17 or newer
- Android SDK 34+

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/diganta/linkmaster.git
   cd linkmaster
   ```

2. Copy configuration template:
   ```bash
   cp keystore.properties.template keystore.properties
   ```

3. Configure `keystore.properties` with your keys:
   - Generate a keystore for release builds
   - Add Firebase configuration (for Pro features)
   - Add AdMob configuration

4. Build the project:
   ```bash
   # Debug build
   ./gradlew assembleDebug
   
   # Release build (requires keystore)
   ./gradlew assembleRelease
   ```

### Windows Users
Use the provided batch script:
```cmd
build_release.bat
```

## 🔧 Configuration

### Firebase Setup (Pro Features)
1. Create a Firebase project
2. Add Android app with package `com.diganta.linkmaster`
3. Download `google-services.json` to `app/`
4. Enable Authentication and Firestore

### AdMob Setup (Free Version)
1. Create AdMob account
2. Create app and ad units
3. Update `keystore.properties` with your IDs

## 📦 Release Process

1. **Version Bump**: Update version in `build.gradle.kts`
2. **Build**: Run `build_release.bat` or `./gradlew bundleRelease`
3. **Test**: Install and test the release APK
4. **Upload**: Upload AAB to Google Play Console
5. **Release**: Submit for review

## 🔒 Privacy & Security

LinkMaster is built with privacy as a core principle:

- **Local First**: All data stored locally by default
- **Optional Cloud**: Cloud sync is opt-in only (Pro feature)
- **Encryption**: All cloud data encrypted with AES-256
- **No Tracking**: No user tracking or analytics without consent
- **Open Source**: Code is open for audit

Read our full [Privacy Policy](PRIVACY_POLICY.md).

## 📄 License & Attribution

LinkMaster is licensed under the MIT License. See [LICENSE_LINKMASTER](LICENSE_LINKMASTER) for details.

### Attribution
LinkMaster is based on [LinkSheet](https://github.com/LinkSheet/LinkSheet) by 1fexd and contributors. We're grateful for their excellent foundation.

**Original Project**: LinkSheet  
**Original License**: LinkSheet Public License Version 1.0  
**Original Repository**: https://github.com/LinkSheet/LinkSheet

**LinkMaster Additions** (MIT Licensed):
- New branding and UI/UX
- Link Analytics system
- QR Code generation
- Encrypted backup
- Social sharing hub
- Monetization features
- Cloud sync functionality

## 🤝 Contributing

We welcome contributions! Please read our [Contributing Guidelines](CONTRIBUTING.md) before submitting PRs.

### Development Setup
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📞 Support

- **Email**: support@diganta.dev
- **GitHub Issues**: [Report bugs or request features](https://github.com/diganta/linkmaster/issues)
- **Privacy**: privacy@diganta.dev

## 🗺 Roadmap

### v1.1 (Q2 2025)
- [ ] Widget support
- [ ] Bulk link operations
- [ ] Export/Import improvements
- [ ] More social platforms

### v1.2 (Q3 2025)
- [ ] Link scheduling
- [ ] Team collaboration
- [ ] API for developers
- [ ] Desktop companion app

## 🙏 Acknowledgments

- **1fexd** and LinkSheet contributors for the excellent foundation
- **Material Design** team for design guidelines
- **Android** team for development tools
- **Open Source Community** for libraries and inspiration

---

<div align="center">
  <strong>Made with ❤️ by Diganta</strong><br>
  <em>Privacy-First Link Management</em>
</div>