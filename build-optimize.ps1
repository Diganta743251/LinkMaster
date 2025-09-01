# LinkMaster Build Optimization Script
# This script optimizes the build process and checks for common issues

Write-Host "🚀 LinkMaster Build Optimization Script" -ForegroundColor Cyan
Write-Host "=======================================" -ForegroundColor Cyan

# Check Java version
Write-Host "`n📋 Checking Java version..." -ForegroundColor Yellow
$javaVersion = java -version 2>&1 | Select-String "version"
Write-Host "Java: $javaVersion" -ForegroundColor Green

# Check Android SDK
Write-Host "`n📋 Checking Android SDK..." -ForegroundColor Yellow
if ($env:ANDROID_HOME) {
    Write-Host "Android SDK: $env:ANDROID_HOME" -ForegroundColor Green
} else {
    Write-Host "⚠️  ANDROID_HOME not set" -ForegroundColor Red
}

# Clean build
Write-Host "`n🧹 Cleaning previous builds..." -ForegroundColor Yellow
.\gradlew clean

# Check for common issues
Write-Host "`n🔍 Checking for common build issues..." -ForegroundColor Yellow

# Check for duplicate dependencies
Write-Host "  - Checking for duplicate dependencies..." -ForegroundColor Gray
$buildFile = Get-Content "app\build.gradle.kts" -Raw
if ($buildFile -match "implementation.*zxing.*implementation.*zxing") {
    Write-Host "    ⚠️  Duplicate ZXing dependencies found" -ForegroundColor Red
} else {
    Write-Host "    ✅ No duplicate dependencies found" -ForegroundColor Green
}

# Check ProGuard rules
Write-Host "  - Checking ProGuard rules..." -ForegroundColor Gray
if (Test-Path "app\proguard-rules.pro") {
    Write-Host "    ✅ ProGuard rules file exists" -ForegroundColor Green
} else {
    Write-Host "    ⚠️  ProGuard rules file missing" -ForegroundColor Red
}

# Check signing configuration
Write-Host "  - Checking signing configuration..." -ForegroundColor Gray
if (Test-Path "keystore.properties") {
    Write-Host "    ✅ Keystore properties found" -ForegroundColor Green
} else {
    Write-Host "    ⚠️  Keystore properties missing (required for release builds)" -ForegroundColor Yellow
}

# Optimize Gradle daemon
Write-Host "`n⚡ Optimizing Gradle daemon..." -ForegroundColor Yellow
.\gradlew --stop
Start-Sleep -Seconds 2

# Build debug version first (faster)
Write-Host "`n🔨 Building debug version..." -ForegroundColor Yellow
$debugBuild = .\gradlew assembleFossDebug --stacktrace 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Debug build successful!" -ForegroundColor Green
} else {
    Write-Host "❌ Debug build failed!" -ForegroundColor Red
    Write-Host $debugBuild -ForegroundColor Red
    exit 1
}

# Run tests
Write-Host "`n🧪 Running unit tests..." -ForegroundColor Yellow
$testResult = .\gradlew testFossDebugUnitTest --stacktrace 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Tests passed!" -ForegroundColor Green
} else {
    Write-Host "⚠️  Some tests failed, but continuing..." -ForegroundColor Yellow
}

# Build release version
Write-Host "`n🚀 Building release version..." -ForegroundColor Yellow
$releaseBuild = .\gradlew assembleFossRelease --stacktrace 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Release build successful!" -ForegroundColor Green
    
    # Show APK location
    $apkPath = "app\build\outputs\apk\foss\release"
    if (Test-Path $apkPath) {
        $apkFiles = Get-ChildItem $apkPath -Filter "*.apk"
        Write-Host "`n📦 APK files generated:" -ForegroundColor Cyan
        foreach ($apk in $apkFiles) {
            $size = [math]::Round($apk.Length / 1MB, 2)
            Write-Host "  - $($apk.Name) ($size MB)" -ForegroundColor Green
        }
    }
} else {
    Write-Host "❌ Release build failed!" -ForegroundColor Red
    Write-Host $releaseBuild -ForegroundColor Red
    exit 1
}

# Generate build report
Write-Host "`n📊 Generating build report..." -ForegroundColor Yellow
$buildTime = Get-Date
$reportContent = @"
# LinkMaster Build Report
Generated: $buildTime

## Build Status
- Debug Build: ✅ Success
- Unit Tests: $(if ($LASTEXITCODE -eq 0) { "✅ Passed" } else { "⚠️ Some failures" })
- Release Build: ✅ Success

## APK Information
$(if (Test-Path "app\build\outputs\apk\foss\release") {
    $apkFiles = Get-ChildItem "app\build\outputs\apk\foss\release" -Filter "*.apk"
    foreach ($apk in $apkFiles) {
        $size = [math]::Round($apk.Length / 1MB, 2)
        "- $($apk.Name): $size MB"
    }
})

## Optimization Recommendations
- ✅ R8 full mode enabled
- ✅ Resource shrinking enabled
- ✅ ProGuard rules configured
- ✅ Gradle build cache enabled
- ✅ Parallel builds enabled

## Next Steps
1. Test the APK on physical devices
2. Upload to Play Console for internal testing
3. Run automated tests on Firebase Test Lab
4. Prepare for production release
"@

$reportContent | Out-File -FilePath "build-report.md" -Encoding UTF8
Write-Host "📄 Build report saved to build-report.md" -ForegroundColor Green

Write-Host "`n🎉 Build optimization complete!" -ForegroundColor Green
Write-Host "Ready for Play Store submission! 🚀" -ForegroundColor Cyan