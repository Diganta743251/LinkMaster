#!/usr/bin/env pwsh

# Comprehensive Verification Script for LinkMaster Upgrades
# This script verifies all the fixes and improvements made

Write-Host "🔍 LinkMaster Comprehensive Verification" -ForegroundColor Cyan
Write-Host "=======================================" -ForegroundColor Cyan

$ErrorActionPreference = "Continue"
$verificationResults = @()

function Add-Result {
    param($Test, $Status, $Details)
    $verificationResults += [PSCustomObject]@{
        Test = $Test
        Status = $Status
        Details = $Details
    }
}

# Navigate to project directory
Set-Location "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"

Write-Host "📍 Verifying from: $(Get-Location)" -ForegroundColor Yellow

# 1. Verify Gradle Version
Write-Host "`n🔧 Checking Gradle Version..." -ForegroundColor Yellow
try {
    $gradleProps = Get-Content "gradle\wrapper\gradle-wrapper.properties" | Where-Object { $_ -match "distributionUrl" }
    if ($gradleProps -match "gradle-8\.11\.1") {
        Add-Result "Gradle Version" "✅ PASS" "Gradle 8.11.1 configured"
        Write-Host "✅ Gradle 8.11.1 - CORRECT" -ForegroundColor Green
    } else {
        Add-Result "Gradle Version" "❌ FAIL" "Gradle version not 8.11.1"
        Write-Host "❌ Gradle version incorrect" -ForegroundColor Red
    }
} catch {
    Add-Result "Gradle Version" "❌ ERROR" $_.Exception.Message
}

# 2. Verify Android Gradle Plugin Version
Write-Host "`n🔧 Checking Android Gradle Plugin..." -ForegroundColor Yellow
try {
    $versionsProps = Get-Content "versions.properties" | Where-Object { $_ -match "plugin\.android=" }
    if ($versionsProps -match "8\.9\.1") {
        Add-Result "AGP Version" "✅ PASS" "Android Gradle Plugin 8.9.1"
        Write-Host "✅ AGP 8.9.1 - CORRECT" -ForegroundColor Green
    } else {
        Add-Result "AGP Version" "❌ FAIL" "AGP version not 8.9.1"
        Write-Host "❌ AGP version incorrect" -ForegroundColor Red
    }
} catch {
    Add-Result "AGP Version" "❌ ERROR" $_.Exception.Message
}

# 3. Verify AndroidManifest.xml fixes
Write-Host "`n📱 Checking AndroidManifest.xml..." -ForegroundColor Yellow
try {
    $manifest = Get-Content "app\src\main\AndroidManifest.xml" -Raw
    $usesCleartextCount = ($manifest | Select-String "usesCleartextTraffic" -AllMatches).Matches.Count
    
    if ($usesCleartextCount -eq 1) {
        Add-Result "AndroidManifest" "✅ PASS" "No duplicate usesCleartextTraffic attributes"
        Write-Host "✅ AndroidManifest.xml - NO DUPLICATES" -ForegroundColor Green
    } else {
        Add-Result "AndroidManifest" "❌ FAIL" "Duplicate usesCleartextTraffic found ($usesCleartextCount instances)"
        Write-Host "❌ AndroidManifest.xml has duplicates" -ForegroundColor Red
    }
} catch {
    Add-Result "AndroidManifest" "❌ ERROR" $_.Exception.Message
}

# 4. Verify Animated Vector Drawable fix
Write-Host "`n🎨 Checking Animated Vector Drawable..." -ForegroundColor Yellow
try {
    $animatedIcon = Get-Content "app\src\main\res\drawable\animated_link_icon.xml" -Raw
    if ($animatedIcon -match 'xmlns:aapt="http://schemas\.android\.com/aapt"' -and 
        $animatedIcon -notmatch '<aapt:attr[^>]*xmlns:aapt=') {
        Add-Result "Animated Vector" "✅ PASS" "AAPT namespace properly declared"
        Write-Host "✅ Animated Vector - NAMESPACE FIXED" -ForegroundColor Green
    } else {
        Add-Result "Animated Vector" "❌ FAIL" "AAPT namespace issues remain"
        Write-Host "❌ Animated Vector namespace issues" -ForegroundColor Red
    }
} catch {
    Add-Result "Animated Vector" "❌ ERROR" $_.Exception.Message
}

# 5. Verify String Resource fixes
Write-Host "`n📝 Checking String Resources..." -ForegroundColor Yellow
try {
    $strings = Get-Content "app\src\main\res\values\strings.xml" -Raw
    $resolveFailedFixed = $strings -match 'name="resolve_failed"[^>]*formatted="false"'
    $resolvedViaFixed = $strings -match 'name="resolved_via"[^>]*formatted="false"'
    
    if ($resolveFailedFixed -and $resolvedViaFixed) {
        Add-Result "String Resources" "✅ PASS" "Multi-substitution strings have formatted='false'"
        Write-Host "✅ String Resources - FORMATTING FIXED" -ForegroundColor Green
    } else {
        Add-Result "String Resources" "❌ FAIL" "String formatting issues remain"
        Write-Host "❌ String formatting not fixed" -ForegroundColor Red
    }
} catch {
    Add-Result "String Resources" "❌ ERROR" $_.Exception.Message
}

# 6. Verify Keystore Configuration
Write-Host "`n🔐 Checking Keystore Configuration..." -ForegroundColor Yellow
try {
    if (Test-Path "keystore.properties") {
        $keystoreProps = Get-Content "keystore.properties" -Raw
        if ($keystoreProps -match "LINKMASTER_KEYSTORE_FILE.*debug\.keystore") {
            Add-Result "Keystore Config" "✅ PASS" "Keystore configuration present"
            Write-Host "✅ Keystore - CONFIGURED" -ForegroundColor Green
        } else {
            Add-Result "Keystore Config" "❌ FAIL" "Keystore configuration incorrect"
            Write-Host "❌ Keystore configuration issues" -ForegroundColor Red
        }
    } else {
        Add-Result "Keystore Config" "❌ FAIL" "keystore.properties not found"
        Write-Host "❌ keystore.properties missing" -ForegroundColor Red
    }
} catch {
    Add-Result "Keystore Config" "❌ ERROR" $_.Exception.Message
}

# 7. Verify Security Files
Write-Host "`n🛡️ Checking Security Files..." -ForegroundColor Yellow
$securityFiles = @(
    "app\src\main\res\xml\network_security_config.xml",
    "app\src\main\res\xml\backup_rules.xml", 
    "app\src\main\res\xml\data_extraction_rules.xml",
    "app\proguard-rules-security.pro",
    "lint.xml"
)

$securityFilesPresent = 0
foreach ($file in $securityFiles) {
    if (Test-Path $file) {
        $securityFilesPresent++
        Write-Host "  ✅ $file" -ForegroundColor Green
    } else {
        Write-Host "  ❌ $file - MISSING" -ForegroundColor Red
    }
}

if ($securityFilesPresent -eq $securityFiles.Count) {
    Add-Result "Security Files" "✅ PASS" "All security files present ($securityFilesPresent/$($securityFiles.Count))"
} else {
    Add-Result "Security Files" "⚠️ PARTIAL" "Some security files missing ($securityFilesPresent/$($securityFiles.Count))"
}

# 8. Verify Build Scripts
Write-Host "`n🚀 Checking Build Scripts..." -ForegroundColor Yellow
$buildScripts = @(
    "build_production.ps1",
    "security_audit.ps1",
    "quick_build.ps1"
)

$buildScriptsPresent = 0
foreach ($script in $buildScripts) {
    if (Test-Path $script) {
        $buildScriptsPresent++
        Write-Host "  ✅ $script" -ForegroundColor Green
    } else {
        Write-Host "  ❌ $script - MISSING" -ForegroundColor Red
    }
}

if ($buildScriptsPresent -eq $buildScripts.Count) {
    Add-Result "Build Scripts" "✅ PASS" "All build scripts present ($buildScriptsPresent/$($buildScripts.Count))"
} else {
    Add-Result "Build Scripts" "⚠️ PARTIAL" "Some build scripts missing ($buildScriptsPresent/$($buildScripts.Count))"
}

# 9. Verify Dependency Versions
Write-Host "`n📦 Checking Dependency Versions..." -ForegroundColor Yellow
try {
    $versions = Get-Content "versions.properties"
    $composeVersion = $versions | Where-Object { $_ -match "version\.androidx\.compose=" } | Select-Object -First 1
    $composeUiVersion = $versions | Where-Object { $_ -match "version\.androidx\.compose\.ui=" } | Select-Object -First 1
    
    $stableVersions = 0
    if ($composeVersion -match "2024\.12\.01") { $stableVersions++ }
    if ($composeUiVersion -match "1\.7\.5") { $stableVersions++ }
    
    if ($stableVersions -eq 2) {
        Add-Result "Dependencies" "✅ PASS" "Key dependencies using stable versions"
        Write-Host "✅ Dependencies - STABLE VERSIONS" -ForegroundColor Green
    } else {
        Add-Result "Dependencies" "⚠️ PARTIAL" "Some dependencies may not be stable"
        Write-Host "⚠️ Some dependencies not stable" -ForegroundColor Yellow
    }
} catch {
    Add-Result "Dependencies" "❌ ERROR" $_.Exception.Message
}

# 10. Check for Documentation
Write-Host "`n📚 Checking Documentation..." -ForegroundColor Yellow
$docFiles = @(
    "GRADLE_UPGRADE_COMPLETE.md",
    "FINAL_UPGRADE_STATUS.md",
    "PRODUCTION_READINESS_SUMMARY.md"
)

$docFilesPresent = 0
foreach ($doc in $docFiles) {
    if (Test-Path $doc) {
        $docFilesPresent++
        Write-Host "  ✅ $doc" -ForegroundColor Green
    } else {
        Write-Host "  ❌ $doc - MISSING" -ForegroundColor Red
    }
}

if ($docFilesPresent -eq $docFiles.Count) {
    Add-Result "Documentation" "✅ PASS" "All documentation present ($docFilesPresent/$($docFiles.Count))"
} else {
    Add-Result "Documentation" "⚠️ PARTIAL" "Some documentation missing ($docFilesPresent/$($docFiles.Count))"
}

# Summary Report
Write-Host "`n" -NoNewline
Write-Host "📊 VERIFICATION SUMMARY" -ForegroundColor Cyan
Write-Host "======================" -ForegroundColor Cyan

$passCount = ($verificationResults | Where-Object { $_.Status -eq "✅ PASS" }).Count
$partialCount = ($verificationResults | Where-Object { $_.Status -eq "⚠️ PARTIAL" }).Count
$failCount = ($verificationResults | Where-Object { $_.Status -eq "❌ FAIL" }).Count
$errorCount = ($verificationResults | Where-Object { $_.Status -eq "❌ ERROR" }).Count
$totalTests = $verificationResults.Count

Write-Host "`n📈 Results:" -ForegroundColor Yellow
Write-Host "  ✅ PASSED: $passCount/$totalTests" -ForegroundColor Green
Write-Host "  ⚠️ PARTIAL: $partialCount/$totalTests" -ForegroundColor Yellow
Write-Host "  ❌ FAILED: $failCount/$totalTests" -ForegroundColor Red
Write-Host "  ❌ ERRORS: $errorCount/$totalTests" -ForegroundColor Red

Write-Host "`n📋 Detailed Results:" -ForegroundColor Yellow
foreach ($result in $verificationResults) {
    Write-Host "  $($result.Status) $($result.Test): $($result.Details)" -ForegroundColor White
}

# Overall Status
$successRate = if ($totalTests -gt 0) { [math]::Round(($passCount / $totalTests) * 100, 1) } else { 0 }
Write-Host "`n🎯 Overall Success Rate: $successRate%" -ForegroundColor $(if ($successRate -ge 80) { "Green" } elseif ($successRate -ge 60) { "Yellow" } else { "Red" })

if ($successRate -ge 90) {
    Write-Host "🎉 EXCELLENT! Your app is ready for production!" -ForegroundColor Green
} elseif ($successRate -ge 80) {
    Write-Host "👍 GOOD! Minor issues to address before production." -ForegroundColor Yellow
} elseif ($successRate -ge 60) {
    Write-Host "⚠️ NEEDS WORK! Several issues need attention." -ForegroundColor Yellow
} else {
    Write-Host "❌ CRITICAL! Major issues need immediate attention." -ForegroundColor Red
}

Write-Host "`n🏁 Verification completed!" -ForegroundColor Cyan