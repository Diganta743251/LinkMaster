# LinkMaster Setup Instructions

This guide will help you set up LinkMaster for development and production deployment.

## 🚀 Quick Start

### 1. Prerequisites
- **Android Studio**: Hedgehog (2023.1.1) or newer
- **JDK**: 17 or newer
- **Android SDK**: API 34+
- **Git**: For version control

### 2. Project Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/diganta/linkmaster.git
   cd linkmaster
   ```

2. **Configure signing**:
   ```bash
   # Copy the template
   cp keystore.properties.template keystore.properties
   
   # Generate a keystore
   keytool -genkey -v -keystore linkmaster.keystore -alias linkmaster -keyalg RSA -keysize 2048 -validity 10000
   ```

3. **Update keystore.properties**:
   ```properties
   LINKMASTER_KEYSTORE_FILE=linkmaster.keystore
   LINKMASTER_KEYSTORE_PASSWORD=your_actual_password
   LINKMASTER_KEY_ALIAS=linkmaster
   LINKMASTER_KEY_PASSWORD=your_actual_key_password
   ```

### 3. Firebase Setup (Pro Features)

1. **Create Firebase Project**:
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create new project: "LinkMaster"
   - Enable Google Analytics (optional)

2. **Add Android App**:
   - Package name: `com.diganta.linkmaster`
   - App nickname: "LinkMaster"
   - SHA-1: Get from Android Studio or keystore

3. **Download Configuration**:
   - Download `google-services.json`
   - Place in `app/` directory

4. **Enable Services**:
   - **Authentication**: Enable Email/Password and Google Sign-In
   - **Firestore**: Create database in production mode
   - **Storage**: Enable for QR code images (optional)

### 4. AdMob Setup (Free Version)

1. **Create AdMob Account**:
   - Go to [AdMob Console](https://apps.admob.com/)
   - Create new app: "LinkMaster"

2. **Create Ad Units**:
   - **Banner Ad**: For bottom banner in free version
   - Note the Ad Unit IDs

3. **Update keystore.properties**:
   ```properties
   ADMOB_APP_ID=ca-app-pub-your_publisher_id~your_app_id
   ADMOB_BANNER_UNIT_ID=ca-app-pub-your_publisher_id/your_banner_unit_id
   ```

### 5. Build the App

#### Debug Build
```bash
./gradlew assembleDebug
```

#### Release Build
```bash
# Windows
build_release.bat

# Linux/Mac
./gradlew bundleRelease
```

## 🔧 Configuration Details

### Database Migration
LinkMaster extends LinkSheet's database from version 19 to 20, adding:
- `link_analytics` table for usage tracking
- `link_folders` table for organization
- Proper indexes for performance

### New Features Configuration

#### 1. Link Analytics
- **Storage**: Local SQLite database
- **Privacy**: No data sent to servers
- **Retention**: Configurable in settings

#### 2. QR Code Generation
- **Library**: ZXing
- **Customization**: Colors, logo overlay
- **Export**: Save to gallery

#### 3. Encrypted Backup
- **Algorithm**: AES-256-GCM
- **Key Derivation**: PBKDF2 with 100,000 iterations
- **Format**: Encrypted JSON

#### 4. Cloud Sync (Pro)
- **Backend**: Firebase Firestore
- **Authentication**: Firebase Auth
- **Encryption**: Client-side before upload

#### 5. Social Sharing
- **Platforms**: WhatsApp, Instagram, Twitter, Telegram, Facebook, LinkedIn, Discord, Reddit
- **Fallback**: Generic Android share intent

## 📱 Google Play Store Setup

### 1. Create Play Console Account
- Go to [Google Play Console](https://play.google.com/console/)
- Pay $25 registration fee
- Complete developer profile

### 2. Create App Listing
- **App Name**: LinkMaster
- **Package Name**: com.diganta.linkmaster
- **Category**: Productivity
- **Content Rating**: Everyone

### 3. Upload Release
```bash
# Build release AAB
./gradlew bundleRelease

# Upload: app/build/outputs/bundle/release/app-release.aab
```

### 4. Store Listing Content

#### Short Description (80 chars)
```
Privacy-first link organizer with QR codes, analytics & cloud sync
```

#### Full Description (4000 chars)
```
🚀 LinkMaster - Smart Link Organizer by Diganta

Organize, analyze, and share your links with privacy-first design and modern features.

✨ FREE FEATURES:
• Smart link handling from any app
• Local analytics and insights
• QR code generation with custom colors
• Link folders and organization
• Three beautiful themes (Light, Dark, Neon)
• Encrypted backup and restore

💎 LINKMASTER PRO:
• Cloud sync across all devices
• Advanced analytics and reports
• Unlimited folders and organization
• Password-protected links
• Custom domains for short links
• Ad-free experience
• Priority support

🔒 PRIVACY FIRST:
• All data stored locally by default
• Optional cloud sync with encryption
• No tracking or data collection
• Open source and auditable

🎨 MODERN DESIGN:
• Material You design language
• Adaptive icons and themes
• Smooth animations and transitions
• Optimized for Android 14+

Perfect for content creators, professionals, and privacy-conscious users who want to organize their digital life efficiently.

Based on LinkSheet with significant enhancements and new features.
```

### 5. Screenshots Requirements
- **Phone**: 16:9 or 9:16 ratio, 1080x1920px minimum
- **Tablet**: 4:3 or 3:4 ratio, 1536x2048px minimum
- **Feature Graphic**: 1024x500px
- **App Icon**: 512x512px

## 🛠 Development Workflow

### 1. Code Structure
```
app/src/main/java/com/diganta/linkmaster/
├── LinkMasterApp.kt              # Main application
├── billing/                      # Monetization
├── dao/                         # Database access
├── database/                    # Database configuration
├── entity/                      # Data models
├── feature/                     # Feature implementations
│   ├── backup/                  # Encrypted backup
│   ├── qr/                      # QR code generation
│   └── social/                  # Social sharing
└── widget/                      # Home widget
```

### 2. Testing
```bash
# Unit tests
./gradlew testDebugUnitTest

# Instrumented tests
./gradlew connectedDebugAndroidTest

# Lint checks
./gradlew lintDebug
```

### 3. Release Checklist
- [ ] Update version in `build.gradle.kts`
- [ ] Test all features thoroughly
- [ ] Run lint and fix issues
- [ ] Generate signed release build
- [ ] Test release APK on multiple devices
- [ ] Update Play Store listing
- [ ] Upload AAB to Play Console
- [ ] Submit for review

## 🔍 Troubleshooting

### Common Issues

#### 1. Build Errors
```bash
# Clean and rebuild
./gradlew clean
./gradlew build
```

#### 2. Firebase Issues
- Ensure `google-services.json` is in `app/` directory
- Check package name matches Firebase configuration
- Verify SHA-1 fingerprint is added to Firebase

#### 3. Keystore Issues
- Ensure keystore file exists and paths are correct
- Check passwords in `keystore.properties`
- Regenerate keystore if corrupted

#### 4. Database Migration Issues
- Clear app data during development
- Check migration scripts in `LinkMasterDatabase.kt`
- Use Room schema export for debugging

### Getting Help
- **GitHub Issues**: [Report bugs](https://github.com/diganta/linkmaster/issues)
- **Email**: support@diganta.dev
- **Documentation**: Check README_LINKMASTER.md

## 📄 Legal Compliance

### Privacy Policy
- Host at: `https://diganta.dev/linkmaster/privacy`
- Update Play Console with privacy policy URL
- Ensure GDPR compliance for EU users

### Attribution
- Keep original LinkSheet attribution
- Add LinkMaster copyright to new code
- Include open source licenses

---

**Ready to build the future of link management! 🚀**