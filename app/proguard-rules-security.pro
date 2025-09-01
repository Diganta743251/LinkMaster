# LinkMaster Security-focused ProGuard Rules
# These rules enhance security while maintaining functionality

# ================================
# SECURITY RULES
# ================================

# Remove all logging in release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# Remove debug-only code
-assumenosideeffects class fe.linksheet.BuildConfig {
    public static final boolean DEBUG return false;
}

# Remove test-only code
-assumenosideeffects class * {
    @androidx.annotation.VisibleForTesting *;
}

# Obfuscate sensitive classes
-keep class fe.linksheet.security.DataProtection {
    public static java.lang.String sanitizeUrlForLogging(java.lang.String);
    public static boolean isUrlSafe(java.lang.String);
    public static boolean validateAppIntegrity(android.content.Context);
}

# Keep security-critical methods but obfuscate implementation
-keepclassmembers class fe.linksheet.security.DataProtection {
    private static *;
}

# ================================
# PERFORMANCE OPTIMIZATIONS
# ================================

# Enable aggressive optimizations
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# Remove unused code
-dontwarn **
-ignorewarnings

# Merge classes and interfaces aggressively
-mergeinterfacesaggressively

# ================================
# KOTLIN SPECIFIC
# ================================

# Keep Kotlin metadata for reflection
-keep class kotlin.Metadata { *; }

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

# Keep Kotlin serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# ================================
# ANDROID SPECIFIC
# ================================

# Keep Android components
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Keep View constructors
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ================================
# COMPOSE SPECIFIC
# ================================

# Keep Compose runtime
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.material3.** { *; }

# Keep Composable functions
-keep @androidx.compose.runtime.Composable class * {
    *;
}

# Keep Compose navigation
-keep class androidx.navigation.** { *; }

# ================================
# ROOM DATABASE
# ================================

# Keep Room entities and DAOs
-keep class fe.linksheet.module.database.entity.** { *; }
-keep class fe.linksheet.module.database.dao.** { *; }
-keep class fe.linksheet.module.database.** { *; }

# Keep Room annotations
-keep class androidx.room.** { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Database class * { *; }

# ================================
# DEPENDENCY INJECTION (KOIN)
# ================================

# Keep Koin modules and definitions
-keep class org.koin.** { *; }
-keep class fe.linksheet.module.** { *; }

# ================================
# NETWORKING & SERIALIZATION
# ================================

# Keep OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Keep Retrofit (if used)
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Keep JSON serialization classes
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# ================================
# QR CODE GENERATION
# ================================

# Keep ZXing classes
-keep class com.google.zxing.** { *; }
-keep class com.journeyapps.barcodescanner.** { *; }
-dontwarn com.google.zxing.**

# ================================
# THIRD PARTY LIBRARIES
# ================================

# Keep Coil image loading
-keep class coil.** { *; }
-dontwarn coil.**

# Keep AndroidX libraries
-keep class androidx.** { *; }
-dontwarn androidx.**

# Keep Google Play Services (if used)
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# ================================
# CUSTOM RULES FOR LINKMASTER
# ================================

# Keep ViewModels
-keep class fe.linksheet.module.viewmodel.** { *; }

# Keep data classes used in UI
-keep class fe.linksheet.composable.page.history.** { *; }

# Keep preference classes
-keep class fe.linksheet.module.preference.** { *; }

# Keep utility classes with public APIs
-keep class fe.linksheet.util.** {
    public *;
}

# ================================
# DEBUGGING (REMOVE IN PRODUCTION)
# ================================

# Uncomment for debugging obfuscation issues
# -printmapping mapping.txt
# -printseeds seeds.txt
# -printusage usage.txt

# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable

# Rename source file attribute to hide original file names
-renamesourcefileattribute SourceFile