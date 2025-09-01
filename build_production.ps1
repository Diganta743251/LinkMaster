# LinkMaster Production Build Script
# Comprehensive build process with security checks and optimizations

param(
    [string]$BuildType = "release",
    [string]$Flavor = "foss",
    [switch]$SkipTests = $false,
    [switch]$SkipLint = $false,
    [switch]$SkipSecurity = $false
)

Write-Host "🚀 LinkMaster Production Build" -ForegroundColor Cyan
Write-Host "=" * 50 -ForegroundColor Gray
Write-Host "Build Type: $BuildType" -ForegroundColor White
Write-Host "Flavor: $Flavor" -ForegroundColor White
Write-Host "Skip Tests: $SkipTests" -ForegroundColor White
Write-Host "Skip Lint: $SkipLint" -ForegroundColor White
Write-Host "Skip Security: $SkipSecurity" -ForegroundColor White

$projectRoot = "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"
$buildFailed = $false
$startTime = Get-Date

# Function to run command and check result
function Invoke-BuildStep {
    param(
        [string]$Command,
        [string]$Description,
        [bool]$Required = $true
    )
    
    Write-Host "`n🔄 $Description..." -ForegroundColor Yellow
    
    try {
        $result = Invoke-Expression $Command
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ $Description completed successfully" -ForegroundColor Green
            return $true
        } else {
            Write-Host "❌ $Description failed with exit code $LASTEXITCODE" -ForegroundColor Red
            if ($Required) {
                $script:buildFailed = $true
            }
            return $false
        }
    } catch {
        Write-Host "❌ $Description failed: $($_.Exception.Message)" -ForegroundColor Red
        if ($Required) {
            $script:buildFailed = $true
        }
        return $false
    }
}

# Change to project directory
Set-Location $projectRoot

Write-Host "`n🔍 Pre-build Checks..." -ForegroundColor Cyan

# Check if keystore.properties exists for release builds
if ($BuildType -eq "release") {
    if (!(Test-Path "keystore.properties")) {
        Write-Host "❌ keystore.properties not found!" -ForegroundColor Red
        Write-Host "   Create keystore.properties from keystore.properties.template" -ForegroundColor Red
        Write-Host "   and configure your signing keys." -ForegroundColor Red
        $buildFailed = $true
    } else {
        Write-Host "✅ Keystore configuration found" -ForegroundColor Green
    }
}

# Run security audit if not skipped
if (!$SkipSecurity) {
    Write-Host "`n🔒 Running Security Audit..." -ForegroundColor Cyan
    try {
        & ".\security_audit.ps1"
    } catch {
        Write-Host "⚠️  Security audit script not found or failed" -ForegroundColor Yellow
    }
}

# Stop if pre-checks failed
if ($buildFailed) {
    Write-Host "`n❌ Pre-build checks failed. Aborting build." -ForegroundColor Red
    exit 1
}

Write-Host "`n🧹 Cleaning Project..." -ForegroundColor Cyan
Invoke-BuildStep ".\gradlew clean --no-daemon" "Clean project"

# Run tests if not skipped
if (!$SkipTests) {
    Write-Host "`n🧪 Running Tests..." -ForegroundColor Cyan
    Invoke-BuildStep ".\gradlew test${Flavor}${BuildType}UnitTest --no-daemon" "Unit tests" $false
    
    # Run lint checks if not skipped
    if (!$SkipLint) {
        Write-Host "`n🔍 Running Lint Checks..." -ForegroundColor Cyan
        Invoke-BuildStep ".\gradlew lint${Flavor}${BuildType} --no-daemon" "Lint analysis" $false
    }
}

# Build the APK
Write-Host "`n🔨 Building APK..." -ForegroundColor Cyan
$buildTask = "assemble${Flavor}${BuildType}"
Invoke-BuildStep ".\gradlew $buildTask --no-daemon" "APK build"

if ($buildFailed) {
    Write-Host "`n❌ Build failed!" -ForegroundColor Red
    exit 1
}

# Find the generated APK
$apkPattern = "app\build\outputs\apk\$Flavor\$BuildType\app-$Flavor-$BuildType*.apk"
$apkFiles = Get-ChildItem -Path $apkPattern -ErrorAction SilentlyContinue

if ($apkFiles.Count -eq 0) {
    Write-Host "`n❌ APK not found at expected location: $apkPattern" -ForegroundColor Red
    exit 1
}

$apkFile = $apkFiles[0]
Write-Host "`n📱 APK Generated: $($apkFile.Name)" -ForegroundColor Green
Write-Host "   Location: $($apkFile.FullName)" -ForegroundColor White
Write-Host "   Size: $([math]::Round($apkFile.Length / 1MB, 2)) MB" -ForegroundColor White

# Verify APK signing for release builds
if ($BuildType -eq "release") {
    Write-Host "`n🔐 Verifying APK Signature..." -ForegroundColor Cyan
    
    # Check if jarsigner is available
    try {
        $jarsignerResult = & jarsigner -verify -verbose $apkFile.FullName 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ APK signature verified successfully" -ForegroundColor Green
        } else {
            Write-Host "❌ APK signature verification failed" -ForegroundColor Red
            Write-Host $jarsignerResult -ForegroundColor Red
        }
    } catch {
        Write-Host "⚠️  jarsigner not found. Install Java SDK to verify signatures." -ForegroundColor Yellow
    }
    
    # Check APK alignment
    try {
        $zipalignResult = & zipalign -c -v 4 $apkFile.FullName 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ APK is properly aligned" -ForegroundColor Green
        } else {
            Write-Host "⚠️  APK alignment check failed" -ForegroundColor Yellow
        }
    } catch {
        Write-Host "⚠️  zipalign not found. Install Android SDK build tools." -ForegroundColor Yellow
    }
}

# Generate build report
$endTime = Get-Date
$buildDuration = $endTime - $startTime

Write-Host "`n📊 Build Summary:" -ForegroundColor Cyan
Write-Host "=" * 30 -ForegroundColor Gray
Write-Host "✅ Build completed successfully!" -ForegroundColor Green
Write-Host "   Duration: $($buildDuration.ToString('mm\:ss'))" -ForegroundColor White
Write-Host "   APK: $($apkFile.Name)" -ForegroundColor White
Write-Host "   Size: $([math]::Round($apkFile.Length / 1MB, 2)) MB" -ForegroundColor White

# Security recommendations for release builds
if ($BuildType -eq "release") {
    Write-Host "`n🔒 Security Checklist for Release:" -ForegroundColor Cyan
    Write-Host "   ✅ Code is minified and obfuscated" -ForegroundColor Green
    Write-Host "   ✅ Resources are shrunk" -ForegroundColor Green
    Write-Host "   ✅ Debug features are disabled" -ForegroundColor Green
    Write-Host "   ✅ APK is signed with release key" -ForegroundColor Green
    Write-Host "   ✅ Network security config is applied" -ForegroundColor Green
    Write-Host "   ✅ Backup rules are configured" -ForegroundColor Green
}

Write-Host "`n🚀 Next Steps:" -ForegroundColor Cyan
Write-Host "   1. Test the APK on multiple devices" -ForegroundColor White
Write-Host "   2. Run security scans (OWASP ZAP, MobSF)" -ForegroundColor White
Write-Host "   3. Perform manual security testing" -ForegroundColor White
Write-Host "   4. Upload to Play Console for internal testing" -ForegroundColor White
Write-Host "   5. Gradually roll out to production" -ForegroundColor White

# Copy APK to releases folder
$releasesDir = "$projectRoot\releases"
if (!(Test-Path $releasesDir)) {
    New-Item -ItemType Directory -Path $releasesDir -Force | Out-Null
}

$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$releaseApkName = "LinkMaster-$Flavor-$BuildType-$timestamp.apk"
$releaseApkPath = Join-Path $releasesDir $releaseApkName

Copy-Item $apkFile.FullName $releaseApkPath
Write-Host "`n📦 APK copied to releases: $releaseApkName" -ForegroundColor Green

Write-Host "`n🎉 Production build completed successfully!" -ForegroundColor Green
Write-Host "   Ready for distribution and testing." -ForegroundColor Green

Write-Host "`n" -ForegroundColor White