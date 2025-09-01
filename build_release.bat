@echo off
REM LinkMaster Release Build Script
REM This script builds a release APK/AAB for Google Play Store

echo ========================================
echo LinkMaster Release Build Script
echo ========================================

REM Check if keystore.properties exists
if not exist "keystore.properties" (
    echo ERROR: keystore.properties not found!
    echo Please copy keystore.properties.template to keystore.properties and configure it.
    pause
    exit /b 1
)

REM Clean previous builds
echo Cleaning previous builds...
call gradlew clean

REM Build release AAB (Android App Bundle) for Play Store
echo Building release AAB...
call gradlew bundleRelease

REM Build release APK for testing
echo Building release APK...
call gradlew assembleRelease

REM Check if build was successful
if exist "app\build\outputs\bundle\release\app-release.aab" (
    echo.
    echo ========================================
    echo BUILD SUCCESSFUL!
    echo ========================================
    echo.
    echo Release AAB: app\build\outputs\bundle\release\app-release.aab
    echo Release APK: app\build\outputs\apk\release\app-release.apk
    echo.
    echo Ready for Google Play Store upload!
    echo.
) else (
    echo.
    echo ========================================
    echo BUILD FAILED!
    echo ========================================
    echo Please check the error messages above.
    echo.
)

pause