# LinkVault - Advanced Link Management App

LinkVault is a comprehensive Android application built with Kotlin and Jetpack Compose for managing, organizing, and analyzing your links with advanced features.

## Features

### 🔗 Link Management
- **Add Links**: Easily add links with automatic metadata fetching
- **Smart Tagging**: Hierarchical tag system (e.g., `work/projects`, `personal/bookmarks`)
- **Custom Slugs**: Create short aliases like `lv://mylink`
- **Rich Metadata**: Title, description, notes, and custom fields
- **Favorites**: Mark important links for quick access

### 🏷️ Advanced Organization
- **Tag Browser**: Navigate through hierarchical tag structure
- **Smart Search**: Search across URLs, titles, tags, and descriptions
- **Multiple Sort Options**: By date, name, or click count
- **Bulk Operations**: Select and delete multiple links

### 📊 Analytics & Insights
- **Click Tracking**: Monitor link usage patterns
- **Usage Statistics**: View most clicked links and recent activity
- **Tag Analytics**: See which tags are most popular
- **Visual Dashboard**: Clean analytics interface

### 🔒 Security & Privacy
- **Biometric Authentication**: Secure app access with fingerprint/face unlock
- **Privacy Controls**: Toggle metadata fetching on/off
- **Local Storage**: All data stored locally using Room database

### 🎨 Modern UI/UX
- **Jetpack Compose**: Modern, responsive UI
- **Material Design**: Clean, intuitive interface
- **Dark Mode Support**: Automatic theme switching
- **QR Code Generation**: Generate QR codes for any link
- **Share Integration**: Easy sharing with other apps

## Technical Stack

### Core Technologies
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose 1.5.4
- **Architecture**: MVVM with Repository pattern
- **Database**: Room 2.6.1 with SQLite
- **Navigation**: Navigation Compose 2.7.6

### Key Dependencies
- **Biometric Authentication**: AndroidX Biometric 1.2.0-alpha05
- **QR Code Generation**: ZXing Android Embedded 4.3.0
- **Image Loading**: Coil 2.5.0
- **Coroutines**: Kotlinx Coroutines 1.7.3
- **ML Kit**: Entity Extraction & Summarization
- **Charts**: MPAndroidChart v3.1.0

## Project Structure

```
app/src/main/java/com/example/link/
├── MainActivity.kt          # Main Compose UI and navigation
├── Link.kt                 # Data model with Room annotations
├── LinkDao.kt              # Database access object
├── LinkViewModel.kt        # ViewModel for UI state management
├── AppDatabase.kt          # Room database configuration
├── LinkDetailActivity.kt   # Legacy activity (now using Compose)
└── LinkAdapter.kt          # Legacy adapter (now using Compose)
```

## Key Features Implementation

### 1. Database Schema
```kotlin
@Entity(tableName = "links")
data class Link(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val url: String,
    val title: String = "",
    val slug: String = "",
    val tags: String = "", // Comma-separated hierarchical tags
    val description: String = "",
    val notes: String = "",
    val clicks: Int = 0,
    val lastClicked: Long? = null,
    val isFavorite: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
```

### 2. Navigation Structure
- **Dashboard**: Main link list with search and sorting
- **Add Link**: Form with metadata fetching
- **Edit Link**: Modify existing links
- **Link Detail**: Full link information with QR code
- **Analytics**: Usage statistics and insights
- **Settings**: App configuration and security
- **Tag Browser**: Hierarchical tag navigation

### 3. Security Implementation
- Biometric authentication using AndroidX Biometric
- Secure local storage with Room encryption
- Privacy controls for metadata fetching

## Building the App

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 17 or later
- Android SDK 34
- Gradle 8.8

### Build Commands
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test

# Install on device
./gradlew installDebug
```

### Configuration
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

## Usage

### Adding Links
1. Tap the floating action button (+)
2. Enter the URL
3. Optionally fetch metadata automatically
4. Add tags using the hierarchical system
5. Set as favorite if needed
6. Save the link

### Organizing with Tags
- Use forward slashes for hierarchy: `work/projects/android`
- Tags are clickable throughout the app
- Browse tags using the dedicated tag browser
- Filter links by specific tags

### Analytics
- View total links and clicks
- See most popular links
- Track recent activity
- Analyze tag usage patterns

### Security
- Enable biometric authentication in settings
- App locks automatically when backgrounded
- All data stored locally for privacy

## Future Enhancements

- [ ] Cloud sync and backup
- [ ] Link sharing between devices
- [ ] Advanced analytics with charts
- [ ] Link categorization with AI
- [ ] Browser extension integration
- [ ] Export/import functionality
- [ ] Link validation and health checks
- [ ] Custom themes and UI customization

## Contributing

This is a complete, production-ready link management application with modern Android development practices and comprehensive features for power users.

## License

This project is built as a demonstration of modern Android development with Kotlin and Jetpack Compose.