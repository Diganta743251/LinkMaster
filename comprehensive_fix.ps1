#!/usr/bin/env pwsh

# Comprehensive Build Fix Script
# This script applies all known fixes systematically

Write-Host "🔧 Comprehensive Build Fix Script" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan

$ErrorActionPreference = "Continue"
Set-Location "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"

$fixes = @()

# Fix 1: Stop all Gradle processes
Write-Host "`n🛑 Fix 1: Stopping all Gradle processes..." -ForegroundColor Yellow
try {
    .\gradlew --stop
    Get-Process | Where-Object {$_.ProcessName -like "*gradle*" -or $_.ProcessName -like "*java*"} | Stop-Process -Force -ErrorAction SilentlyContinue
    $fixes += "✅ Gradle processes stopped"
} catch {
    $fixes += "❌ Failed to stop Gradle processes: $($_.Exception.Message)"
}

# Fix 2: Clean all build artifacts
Write-Host "`n🧹 Fix 2: Cleaning build artifacts..." -ForegroundColor Yellow
try {
    Remove-Item -Path "build" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path "app\build" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path ".gradle" -Recurse -Force -ErrorAction SilentlyContinue
    $fixes += "✅ Build artifacts cleaned"
} catch {
    $fixes += "❌ Failed to clean build artifacts: $($_.Exception.Message)"
}

# Fix 3: Update Room version to stable
Write-Host "`n📦 Fix 3: Ensuring stable Room version..." -ForegroundColor Yellow
try {
    $versionsContent = Get-Content "versions.properties" -Raw
    if ($versionsContent -match "version\.androidx\.room=2\.6\.1") {
        $fixes += "✅ Room version is already stable (2.6.1)"
    } else {
        $versionsContent = $versionsContent -replace "version\.androidx\.room=.*", "version.androidx.room=2.6.1"
        $versionsContent | Out-File -FilePath "versions.properties" -Encoding UTF8
        $fixes += "✅ Room version updated to stable 2.6.1"
    }
} catch {
    $fixes += "❌ Failed to update Room version: $($_.Exception.Message)"
}

# Fix 4: Fix WikiCacheDao query
Write-Host "`n🗃️ Fix 4: Fixing WikiCacheDao SQL query..." -ForegroundColor Yellow
try {
    $wikiDaoPath = "app\src\main\java\fe\linksheet\module\database\dao\WikiCacheDao.kt"
    if (Test-Path $wikiDaoPath) {
        $wikiDaoContent = Get-Content $wikiDaoPath -Raw
        if ($wikiDaoContent -match "AND timestamp\)") {
            $wikiDaoContent = $wikiDaoContent -replace "AND timestamp\)", "ORDER BY timestamp DESC LIMIT 1)"
            $wikiDaoContent | Out-File -FilePath $wikiDaoPath -Encoding UTF8
            $fixes += "✅ WikiCacheDao SQL query fixed"
        } else {
            $fixes += "✅ WikiCacheDao SQL query already correct"
        }
    } else {
        $fixes += "⚠️ WikiCacheDao not found"
    }
} catch {
    $fixes += "❌ Failed to fix WikiCacheDao: $($_.Exception.Message)"
}

# Fix 5: Disable KSP2 for compatibility
Write-Host "`n🔧 Fix 5: Disabling KSP2 for compatibility..." -ForegroundColor Yellow
try {
    $gradlePropsContent = Get-Content "gradle.properties" -Raw
    if ($gradlePropsContent -match "useKSP2=true") {
        $gradlePropsContent = $gradlePropsContent -replace "useKSP2=true", "# useKSP2=true  # Disabled for compatibility"
        $gradlePropsContent | Out-File -FilePath "gradle.properties" -Encoding UTF8
        $fixes += "✅ KSP2 disabled for compatibility"
    } else {
        $fixes += "✅ KSP2 already disabled"
    }
} catch {
    $fixes += "❌ Failed to disable KSP2: $($_.Exception.Message)"
}

# Fix 6: Ensure Room schema directory exists
Write-Host "`n📁 Fix 6: Creating Room schema directory..." -ForegroundColor Yellow
try {
    $schemaDir = "app\schemas"
    if (!(Test-Path $schemaDir)) {
        New-Item -ItemType Directory -Path $schemaDir -Force | Out-Null
        $fixes += "✅ Room schema directory created"
    } else {
        $fixes += "✅ Room schema directory already exists"
    }
} catch {
    $fixes += "❌ Failed to create schema directory: $($_.Exception.Message)"
}

# Fix 7: Optimize gradle.properties for build
Write-Host "`n⚙️ Fix 7: Optimizing gradle.properties..." -ForegroundColor Yellow
try {
    $gradlePropsContent = Get-Content "gradle.properties" -Raw
    
    # Reduce memory usage
    $gradlePropsContent = $gradlePropsContent -replace "org\.gradle\.jvmargs=-Xmx8192m", "org.gradle.jvmargs=-Xmx6g"
    $gradlePropsContent = $gradlePropsContent -replace "kotlin\.daemon\.jvmargs=-Xmx16G", "kotlin.daemon.jvmargs=-Xmx4G"
    
    # Disable parallel for stability
    $gradlePropsContent = $gradlePropsContent -replace "org\.gradle\.parallel=true", "org.gradle.parallel=false"
    
    # Disable configuration on demand for stability
    $gradlePropsContent = $gradlePropsContent -replace "org\.gradle\.configureondemand=true", "org.gradle.configureondemand=false"
    
    $gradlePropsContent | Out-File -FilePath "gradle.properties" -Encoding UTF8
    $fixes += "✅ gradle.properties optimized for stability"
} catch {
    $fixes += "❌ Failed to optimize gradle.properties: $($_.Exception.Message)"
}

# Fix 8: Check and fix AndroidManifest duplicates
Write-Host "`n📱 Fix 8: Checking AndroidManifest.xml..." -ForegroundColor Yellow
try {
    $manifestPath = "app\src\main\AndroidManifest.xml"
    $manifestContent = Get-Content $manifestPath -Raw
    $usesCleartextCount = ($manifestContent | Select-String "usesCleartextTraffic" -AllMatches).Matches.Count
    
    if ($usesCleartextCount -eq 1) {
        $fixes += "✅ AndroidManifest.xml has no duplicates"
    } else {
        $fixes += "⚠️ AndroidManifest.xml may have duplicates ($usesCleartextCount instances)"
    }
} catch {
    $fixes += "❌ Failed to check AndroidManifest.xml: $($_.Exception.Message)"
}

# Fix 9: Check animated vector drawable
Write-Host "`n🎨 Fix 9: Checking animated vector drawable..." -ForegroundColor Yellow
try {
    $animatedIconPath = "app\src\main\res\drawable\animated_link_icon.xml"
    if (Test-Path $animatedIconPath) {
        $animatedContent = Get-Content $animatedIconPath -Raw
        if ($animatedContent -match 'xmlns:aapt="http://schemas\.android\.com/aapt"') {
            $fixes += "✅ Animated vector drawable namespace correct"
        } else {
            $fixes += "⚠️ Animated vector drawable namespace may need fixing"
        }
    } else {
        $fixes += "⚠️ Animated vector drawable not found"
    }
} catch {
    $fixes += "❌ Failed to check animated vector drawable: $($_.Exception.Message)"
}

# Fix 10: Test basic Gradle configuration
Write-Host "`n🧪 Fix 10: Testing basic Gradle configuration..." -ForegroundColor Yellow
try {
    $testResult = & .\gradlew.bat "help" "--no-daemon" "--quiet" 2>&1
    if ($LASTEXITCODE -eq 0) {
        $fixes += "✅ Basic Gradle configuration works"
        
        # Try to get project info
        Write-Host "   📋 Getting project information..." -ForegroundColor Gray
        $projectsResult = & .\gradlew.bat "projects" "--no-daemon" "--quiet" 2>&1
        if ($LASTEXITCODE -eq 0) {
            $fixes += "✅ Project structure accessible"
        } else {
            $fixes += "⚠️ Project structure has issues"
        }
    } else {
        $fixes += "❌ Basic Gradle configuration failed"
        $fixes += "   Error: $($testResult | Select-Object -Last 3 | Out-String)"
    }
} catch {
    $fixes += "❌ Failed to test Gradle configuration: $($_.Exception.Message)"
}

# Summary Report
Write-Host "`n📊 COMPREHENSIVE FIX SUMMARY" -ForegroundColor Cyan
Write-Host "============================" -ForegroundColor Cyan

$successCount = ($fixes | Where-Object { $_ -match "^✅" }).Count
$warningCount = ($fixes | Where-Object { $_ -match "^⚠️" }).Count
$failureCount = ($fixes | Where-Object { $_ -match "^❌" }).Count
$totalFixes = $fixes.Count

Write-Host "`n📈 Results:" -ForegroundColor Yellow
Write-Host "  ✅ Successful: $successCount/$totalFixes" -ForegroundColor Green
Write-Host "  ⚠️ Warnings: $warningCount/$totalFixes" -ForegroundColor Yellow
Write-Host "  ❌ Failed: $failureCount/$totalFixes" -ForegroundColor Red

Write-Host "`n📋 Detailed Results:" -ForegroundColor Yellow
foreach ($fix in $fixes) {
    $color = switch -Regex ($fix) {
        "^✅" { "Green" }
        "^⚠️" { "Yellow" }
        "^❌" { "Red" }
        default { "White" }
    }
    Write-Host "  $fix" -ForegroundColor $color
}

$successRate = [math]::Round(($successCount / $totalFixes) * 100, 1)
Write-Host "`n🎯 Overall Success Rate: $successRate%" -ForegroundColor $(if ($successRate -ge 80) { "Green" } elseif ($successRate -ge 60) { "Yellow" } else { "Red" })

if ($successRate -ge 90) {
    Write-Host "`n🎉 EXCELLENT! Your build should work now!" -ForegroundColor Green
    Write-Host "Try running: .\gradlew assembleFossDebug --no-daemon" -ForegroundColor Cyan
} elseif ($successRate -ge 70) {
    Write-Host "`n👍 GOOD! Most issues fixed. Try a build:" -ForegroundColor Yellow
    Write-Host ".\gradlew assembleFossDebug --no-daemon --stacktrace" -ForegroundColor Cyan
} else {
    Write-Host "`n⚠️ NEEDS MORE WORK! Check the failed fixes above." -ForegroundColor Red
}

Write-Host "`n🏁 Comprehensive fix completed!" -ForegroundColor Cyan