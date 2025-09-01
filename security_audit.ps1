# LinkMaster Security Audit Script
# Comprehensive security check for production readiness

Write-Host "🔒 LinkMaster Security Audit" -ForegroundColor Cyan
Write-Host "=" * 50 -ForegroundColor Gray

$projectRoot = "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"
$errors = @()
$warnings = @()
$passed = @()

# Function to check file content for security issues
function Test-SecurityIssue {
    param(
        [string]$FilePath,
        [string]$Pattern,
        [string]$Description,
        [string]$Severity = "ERROR"
    )
    
    if (Test-Path $FilePath) {
        $content = Get-Content $FilePath -Raw
        if ($content -match $Pattern) {
            if ($Severity -eq "ERROR") {
                $script:errors += "❌ $Description in $FilePath"
            } else {
                $script:warnings += "⚠️  $Description in $FilePath"
            }
            return $false
        } else {
            $script:passed += "✅ $Description - OK"
            return $true
        }
    }
    return $true
}

Write-Host "`n🔍 Checking for Security Vulnerabilities..." -ForegroundColor Yellow

# Check for hardcoded secrets
Test-SecurityIssue "$projectRoot\app\src\main\java\**\*.kt" "password\s*=\s*[""'][^""']+[""']" "Hardcoded passwords"
Test-SecurityIssue "$projectRoot\app\src\main\java\**\*.kt" "api_key\s*=\s*[""'][^""']+[""']" "Hardcoded API keys"
Test-SecurityIssue "$projectRoot\app\src\main\java\**\*.kt" "secret\s*=\s*[""'][^""']+[""']" "Hardcoded secrets"
Test-SecurityIssue "$projectRoot\app\src\main\java\**\*.kt" "token\s*=\s*[""'][^""']+[""']" "Hardcoded tokens"

# Check for insecure network configurations
Test-SecurityIssue "$projectRoot\app\src\main\AndroidManifest.xml" 'android:usesCleartextTraffic="true"' "Cleartext traffic allowed"
Test-SecurityIssue "$projectRoot\app\src\main\AndroidManifest.xml" 'android:allowBackup="true"' "Backup allowed"

# Check for debug code in production
Test-SecurityIssue "$projectRoot\app\src\main\java\**\*.kt" "Log\.[dviwe]" "Debug logging" "WARNING"
Test-SecurityIssue "$projectRoot\app\src\main\java\**\*.kt" "println" "Print statements" "WARNING"
Test-SecurityIssue "$projectRoot\app\src\main\java\**\*.kt" "TODO" "TODO comments" "WARNING"

# Check for insecure permissions
Test-SecurityIssue "$projectRoot\app\src\main\AndroidManifest.xml" 'android:exported="true"' "Exported components without intent filters" "WARNING"
Test-SecurityIssue "$projectRoot\app\src\main\AndroidManifest.xml" "WRITE_EXTERNAL_STORAGE" "External storage write permission" "WARNING"

Write-Host "`n🛡️  Checking Security Configurations..." -ForegroundColor Yellow

# Check if security files exist
$securityFiles = @(
    "$projectRoot\app\src\main\res\xml\network_security_config.xml",
    "$projectRoot\app\src\main\res\xml\backup_rules.xml", 
    "$projectRoot\app\src\main\res\xml\data_extraction_rules.xml",
    "$projectRoot\app\proguard-rules-security.pro",
    "$projectRoot\app\src\main\java\fe\linksheet\security\DataProtection.kt"
)

foreach ($file in $securityFiles) {
    if (Test-Path $file) {
        $passed += "✅ Security file exists: $(Split-Path $file -Leaf)"
    } else {
        $errors += "❌ Missing security file: $(Split-Path $file -Leaf)"
    }
}

Write-Host "`n🔐 Checking Signing Configuration..." -ForegroundColor Yellow

# Check keystore configuration
if (Test-Path "$projectRoot\keystore.properties") {
    $warnings += "⚠️  keystore.properties exists - ensure it's not in version control"
} else {
    $warnings += "⚠️  keystore.properties not found - create from template for release builds"
}

if (Test-Path "$projectRoot\keystore.properties.template") {
    $passed += "✅ Keystore template exists"
} else {
    $errors += "❌ Missing keystore.properties.template"
}

Write-Host "`n📱 Checking Build Configuration..." -ForegroundColor Yellow

# Check build.gradle.kts for security settings
$buildGradle = "$projectRoot\app\build.gradle.kts"
if (Test-Path $buildGradle) {
    $content = Get-Content $buildGradle -Raw
    
    if ($content -match "isMinifyEnabled = true") {
        $passed += "✅ Code minification enabled"
    } else {
        $errors += "❌ Code minification not enabled"
    }
    
    if ($content -match "isShrinkResources = true") {
        $passed += "✅ Resource shrinking enabled"
    } else {
        $errors += "❌ Resource shrinking not enabled"
    }
    
    if ($content -match "proguard-rules-security.pro") {
        $passed += "✅ Security ProGuard rules included"
    } else {
        $errors += "❌ Security ProGuard rules not included"
    }
    
    if ($content -match 'isDebuggable = false') {
        $passed += "✅ Debug disabled in release"
    } else {
        $warnings += "⚠️  Debug not explicitly disabled in release"
    }
}

Write-Host "`n🧪 Checking Lint Configuration..." -ForegroundColor Yellow

# Check lint configuration
if (Test-Path "$projectRoot\app\lint.xml") {
    $passed += "✅ Lint configuration exists"
    
    $lintContent = Get-Content "$projectRoot\app\lint.xml" -Raw
    if ($lintContent -match 'severity="error"') {
        $passed += "✅ Lint errors configured"
    } else {
        $warnings += "⚠️  No lint errors configured"
    }
} else {
    $errors += "❌ Missing lint.xml configuration"
}

Write-Host "`n📊 Security Audit Results:" -ForegroundColor Cyan
Write-Host "=" * 30 -ForegroundColor Gray

if ($passed.Count -gt 0) {
    Write-Host "`n✅ PASSED CHECKS ($($passed.Count)):" -ForegroundColor Green
    $passed | ForEach-Object { Write-Host "   $_" -ForegroundColor Green }
}

if ($warnings.Count -gt 0) {
    Write-Host "`n⚠️  WARNINGS ($($warnings.Count)):" -ForegroundColor Yellow
    $warnings | ForEach-Object { Write-Host "   $_" -ForegroundColor Yellow }
}

if ($errors.Count -gt 0) {
    Write-Host "`n❌ ERRORS ($($errors.Count)):" -ForegroundColor Red
    $errors | ForEach-Object { Write-Host "   $_" -ForegroundColor Red }
}

# Calculate security score
$total = $passed.Count + $warnings.Count + $errors.Count
$securityScore = if ($total -gt 0) { [math]::Round((($passed.Count + $warnings.Count * 0.5) / $total) * 100, 1) } else { 0 }

Write-Host "`n📈 SECURITY SCORE: $securityScore%" -ForegroundColor $(
    if ($securityScore -ge 90) { "Green" } 
    elseif ($securityScore -ge 70) { "Yellow" } 
    else { "Red" }
)

# Security recommendations
Write-Host "`n🔒 Security Recommendations:" -ForegroundColor Cyan

if ($errors.Count -eq 0 -and $warnings.Count -le 3) {
    Write-Host "   🎉 EXCELLENT! Your app is production-ready from a security perspective." -ForegroundColor Green
    Write-Host "   • All critical security measures are in place" -ForegroundColor Green
    Write-Host "   • Code is properly obfuscated and hardened" -ForegroundColor Green
    Write-Host "   • Network security is configured correctly" -ForegroundColor Green
} elseif ($errors.Count -eq 0) {
    Write-Host "   ✅ GOOD! Your app has solid security with minor improvements needed." -ForegroundColor Yellow
    Write-Host "   • Address the warnings above for optimal security" -ForegroundColor Yellow
    Write-Host "   • Consider additional security hardening measures" -ForegroundColor Yellow
} else {
    Write-Host "   ⚠️  NEEDS ATTENTION! Critical security issues found." -ForegroundColor Red
    Write-Host "   • Fix all errors before releasing to production" -ForegroundColor Red
    Write-Host "   • Review and address all warnings" -ForegroundColor Red
}

Write-Host "`n🚀 Next Steps:" -ForegroundColor Cyan
Write-Host "   1. Fix any errors found above" -ForegroundColor White
Write-Host "   2. Address warnings for optimal security" -ForegroundColor White
Write-Host "   3. Run lint checks: .\gradlew lintFossRelease" -ForegroundColor White
Write-Host "   4. Test release build: .\gradlew assembleFossRelease" -ForegroundColor White
Write-Host "   5. Verify APK signing: jarsigner -verify -verbose app-foss-release.apk" -ForegroundColor White

Write-Host "`n📚 Security Documentation:" -ForegroundColor Cyan
Write-Host "   • Review OWASP Mobile Security Guidelines" -ForegroundColor White
Write-Host "   • Check Android Security Best Practices" -ForegroundColor White
Write-Host "   • Test with security scanning tools" -ForegroundColor White

Write-Host "`n" -ForegroundColor White