#!/usr/bin/env pwsh

# Quick Build Script for LinkMaster
# This script attempts to build the app with minimal configuration

Write-Host "🚀 LinkMaster Quick Build Script" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan

# Set error handling
$ErrorActionPreference = "Continue"

# Navigate to project directory
Set-Location "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"

Write-Host "📍 Current directory: $(Get-Location)" -ForegroundColor Yellow

# Stop any existing Gradle daemons
Write-Host "🛑 Stopping Gradle daemons..." -ForegroundColor Yellow
try {
    .\gradlew --stop --quiet
    Write-Host "✅ Gradle daemons stopped" -ForegroundColor Green
} catch {
    Write-Host "⚠️ Could not stop daemons: $($_.Exception.Message)" -ForegroundColor Yellow
}

# Clean build directory
Write-Host "🧹 Cleaning build directories..." -ForegroundColor Yellow
try {
    Remove-Item -Path "build" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path "app\build" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path ".gradle" -Recurse -Force -ErrorAction SilentlyContinue
    Write-Host "✅ Build directories cleaned" -ForegroundColor Green
} catch {
    Write-Host "⚠️ Could not clean all directories: $($_.Exception.Message)" -ForegroundColor Yellow
}

# Check if keystore exists
$keystorePath = "C:\Users\Diganta1\.android\debug.keystore"
if (Test-Path $keystorePath) {
    Write-Host "✅ Debug keystore found at: $keystorePath" -ForegroundColor Green
} else {
    Write-Host "⚠️ Debug keystore not found. Creating one..." -ForegroundColor Yellow
    try {
        # Create .android directory if it doesn't exist
        $androidDir = "C:\Users\Diganta1\.android"
        if (!(Test-Path $androidDir)) {
            New-Item -ItemType Directory -Path $androidDir -Force
        }
        
        # Generate debug keystore
        keytool -genkey -v -keystore $keystorePath -alias androiddebugkey -keyalg RSA -keysize 2048 -validity 10000 -storepass android -keypass android -dname "CN=Android Debug,O=Android,C=US"
        Write-Host "✅ Debug keystore created" -ForegroundColor Green
    } catch {
        Write-Host "❌ Could not create debug keystore: $($_.Exception.Message)" -ForegroundColor Red
    }
}
# Try to build with minimal options
Write-Host "🔨 Attempting to build debug APK..." -ForegroundColor Yellow
Write-Host "This may take several minutes..." -ForegroundColor Gray

try {
    # Use timeout to prevent hanging
    $buildProcess = Start-Process -FilePath ".\gradlew.bat" -ArgumentList "assembleDebug", "--no-daemon", "--stacktrace", "--info" -PassThru -NoNewWindow -RedirectStandardOutput "build_output.log" -RedirectStandardError "build_error.log"
    
    # Wait for up to 10 minutes
    $timeout = 600
    if ($buildProcess.WaitForExit($timeout * 1000)) {
        if ($buildProcess.ExitCode -eq 0) {
            Write-Host "✅ Build completed successfully!" -ForegroundColor Green
            
            # Check for APK files
            $apkFiles = Get-ChildItem -Path "app\build\outputs\apk" -Recurse -Filter "*.apk" -ErrorAction SilentlyContinue
            if ($apkFiles) {
                Write-Host "📱 APK files generated:" -ForegroundColor Green
                foreach ($apk in $apkFiles) {
                    $sizeKB = [math]::Round($apk.Length / 1KB, 2)
                    Write-Host "   📦 $($apk.Name) - ${sizeKB} KB" -ForegroundColor Cyan
                }
            } else {
                Write-Host "⚠️ No APK files found in output directory" -ForegroundColor Yellow
            }
        } else {
            Write-Host "❌ Build failed with exit code: $($buildProcess.ExitCode)" -ForegroundColor Red
            Write-Host "📋 Check build_output.log and build_error.log for details" -ForegroundColor Yellow
        }
    } else {
        Write-Host "⏰ Build timed out after $timeout seconds" -ForegroundColor Red
        $buildProcess.Kill()
    }
} catch {
    Write-Host "❌ Build process failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Show build logs if they exist
if (Test-Path "build_output.log") {
    $outputSize = (Get-Item "build_output.log").Length
    if ($outputSize -gt 0) {
        Write-Host "📋 Last 20 lines of build output:" -ForegroundColor Yellow
        Get-Content "build_output.log" | Select-Object -Last 20
    }
}

if (Test-Path "build_error.log") {
    $errorSize = (Get-Item "build_error.log").Length
    if ($errorSize -gt 0) {
        Write-Host "❌ Build errors:" -ForegroundColor Red
        Get-Content "build_error.log" | Select-Object -Last 10
    }
}

Write-Host "🏁 Quick build script completed" -ForegroundColor Cyan