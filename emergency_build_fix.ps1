#!/usr/bin/env pwsh

# Emergency Build Fix Script
# This script applies aggressive fixes to get the build working

Write-Host "🚨 Emergency Build Fix Script" -ForegroundColor Red
Write-Host "=============================" -ForegroundColor Red

$ErrorActionPreference = "Continue"
Set-Location "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"

# 1. Stop all Gradle processes
Write-Host "🛑 Stopping all Gradle processes..." -ForegroundColor Yellow
try {
    .\gradlew --stop
    Get-Process | Where-Object {$_.ProcessName -like "*gradle*" -or $_.ProcessName -like "*java*"} | Stop-Process -Force -ErrorAction SilentlyContinue
    Write-Host "✅ All processes stopped" -ForegroundColor Green
} catch {
    Write-Host "⚠️ Some processes couldn't be stopped" -ForegroundColor Yellow
}

# 2. Clean all build artifacts
Write-Host "🧹 Cleaning all build artifacts..." -ForegroundColor Yellow
try {
    Remove-Item -Path "build" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path "app\build" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path ".gradle" -Recurse -Force -ErrorAction SilentlyContinue
    Get-ChildItem -Path "." -Recurse -Directory -Name "build" | ForEach-Object { Remove-Item -Path $_ -Recurse -Force -ErrorAction SilentlyContinue }
    Write-Host "✅ Build artifacts cleaned" -ForegroundColor Green
} catch {
    Write-Host "⚠️ Some artifacts couldn't be cleaned" -ForegroundColor Yellow
}

# 3. Create optimized gradle.properties for build
Write-Host "⚙️ Creating optimized gradle.properties..." -ForegroundColor Yellow
$optimizedGradleProps = @"
# Optimized Gradle Properties for Emergency Build
org.gradle.jvmargs=-Xmx6g -Dfile.encoding=UTF-8 -XX:+UseG1GC
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=false
android.useAndroidX=true
kotlin.code.style=official
kotlin.daemon.jvmargs=-Xmx4G
android.nonTransitiveRClass=true
android.nonFinalResIds=false
android.enableR8.fullMode=false
# useKSP2=false  # Disabled for compatibility
gradle.build.version=0.0.67

# Performance optimizations
android.defaults.buildfeatures.buildconfig=false
android.defaults.buildfeatures.aidl=false
android.defaults.buildfeatures.renderscript=false
android.defaults.buildfeatures.resvalues=false
android.defaults.buildfeatures.shaders=false

# Suppress warnings
android.suppressUnsupportedCompileSdk=36

# Build plugins
gradle.build.plugins=\
  com.gitlab.grrfe.build-settings-plugin=com.gitlab.grrfe.gradle-build:build-settings,\
  com.gitlab.grrfe.new-build-logic-plugin=com.gitlab.grrfe.gradle-build:new-build-logic,\
  com.gitlab.grrfe.library-build-plugin=com.gitlab.grrfe.gradle-build:new-build-logic
"@

try {
    Copy-Item "gradle.properties" "gradle.properties.original" -Force
    $optimizedGradleProps | Out-File -FilePath "gradle.properties" -Encoding UTF8
    Write-Host "✅ Optimized gradle.properties created" -ForegroundColor Green
} catch {
    Write-Host "❌ Failed to create optimized gradle.properties" -ForegroundColor Red
}

# 4. Try a minimal build first
Write-Host "🔨 Attempting minimal build..." -ForegroundColor Yellow
try {
    $buildResult = & .\gradlew.bat "help" "--no-daemon" "--quiet" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Gradle configuration successful" -ForegroundColor Green
        
        # Try building just the config module first
        Write-Host "🔨 Building config module..." -ForegroundColor Yellow
        $configResult = & .\gradlew.bat ":config:build" "--no-daemon" "--quiet" 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Config module built successfully" -ForegroundColor Green
            
            # Try building the app
            Write-Host "🔨 Building app module..." -ForegroundColor Yellow
            $appResult = & .\gradlew.bat "assembleFossDebug" "--no-daemon" "--stacktrace" 2>&1
            if ($LASTEXITCODE -eq 0) {
                Write-Host "🎉 BUILD SUCCESSFUL!" -ForegroundColor Green
                
                # Check for APK
                $apkFiles = Get-ChildItem -Path "app\build\outputs\apk" -Recurse -Filter "*.apk" -ErrorAction SilentlyContinue
                if ($apkFiles) {
                    Write-Host "📱 APK files generated:" -ForegroundColor Green
                    foreach ($apk in $apkFiles) {
                        $sizeKB = [math]::Round($apk.Length / 1KB, 2)
                        Write-Host "   📦 $($apk.Name) - ${sizeKB} KB" -ForegroundColor Cyan
                    }
                } else {
                    Write-Host "⚠️ No APK files found" -ForegroundColor Yellow
                }
            } else {
                Write-Host "❌ App build failed" -ForegroundColor Red
                Write-Host "Error output:" -ForegroundColor Red
                Write-Host $appResult -ForegroundColor Red
            }
        } else {
            Write-Host "❌ Config module build failed" -ForegroundColor Red
            Write-Host $configResult -ForegroundColor Red
        }
    } else {
        Write-Host "❌ Gradle configuration failed" -ForegroundColor Red
        Write-Host $buildResult -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Build process failed: $($_.Exception.Message)" -ForegroundColor Red
}

# 5. Restore original gradle.properties if build failed
if ($LASTEXITCODE -ne 0 -and (Test-Path "gradle.properties.original")) {
    Write-Host "🔄 Restoring original gradle.properties..." -ForegroundColor Yellow
    Copy-Item "gradle.properties.original" "gradle.properties" -Force
}

Write-Host "🏁 Emergency build fix completed" -ForegroundColor Cyan
Write-Host "Exit code: $LASTEXITCODE" -ForegroundColor $(if ($LASTEXITCODE -eq 0) { "Green" } else { "Red" })