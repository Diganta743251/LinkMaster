#!/usr/bin/env pwsh

# Simple Build Test - Bypass Complex Plugins
Write-Host "🧪 Simple Build Test" -ForegroundColor Cyan
Write-Host "===================" -ForegroundColor Cyan

$ErrorActionPreference = "Continue"
Set-Location "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"

# Create a minimal gradle.properties for testing
$minimalGradleProps = @"
# Minimal Gradle Properties for Testing
org.gradle.jvmargs=-Xmx4g -Dfile.encoding=UTF-8
org.gradle.parallel=false
org.gradle.caching=false
org.gradle.configureondemand=false
android.useAndroidX=true
kotlin.code.style=official
android.nonTransitiveRClass=true
android.nonFinalResIds=false
android.enableR8.fullMode=false
android.suppressUnsupportedCompileSdk=36
"@

Write-Host "📝 Creating minimal gradle.properties..." -ForegroundColor Yellow
try {
    Copy-Item "gradle.properties" "gradle.properties.backup2" -Force -ErrorAction SilentlyContinue
    $minimalGradleProps | Out-File -FilePath "gradle.properties" -Encoding UTF8
    Write-Host "✅ Minimal gradle.properties created" -ForegroundColor Green
} catch {
    Write-Host "❌ Failed to create minimal gradle.properties" -ForegroundColor Red
}

# Try to run just gradle help to see if basic configuration works
Write-Host "🔧 Testing basic Gradle configuration..." -ForegroundColor Yellow
try {
    $helpResult = & .\gradlew.bat "help" "--no-daemon" "--quiet" "--stacktrace" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Basic Gradle configuration works!" -ForegroundColor Green
        
        # Try to list tasks
        Write-Host "📋 Listing available tasks..." -ForegroundColor Yellow
        $tasksResult = & .\gradlew.bat "tasks" "--no-daemon" "--quiet" 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Tasks listed successfully" -ForegroundColor Green
            Write-Host "Available build tasks:" -ForegroundColor Cyan
            $tasksResult | Where-Object { $_ -match "assemble|build" } | ForEach-Object { Write-Host "  $_" -ForegroundColor White }
        } else {
            Write-Host "❌ Failed to list tasks" -ForegroundColor Red
            Write-Host $tasksResult -ForegroundColor Red
        }
    } else {
        Write-Host "❌ Basic Gradle configuration failed" -ForegroundColor Red
        Write-Host "Error details:" -ForegroundColor Red
        Write-Host $helpResult -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Exception during Gradle test: $($_.Exception.Message)" -ForegroundColor Red
}

# Restore original gradle.properties
Write-Host "🔄 Restoring original gradle.properties..." -ForegroundColor Yellow
try {
    if (Test-Path "gradle.properties.backup2") {
        Copy-Item "gradle.properties.backup2" "gradle.properties" -Force
        Remove-Item "gradle.properties.backup2" -Force -ErrorAction SilentlyContinue
        Write-Host "✅ Original gradle.properties restored" -ForegroundColor Green
    }
} catch {
    Write-Host "⚠️ Could not restore original gradle.properties" -ForegroundColor Yellow
}

Write-Host "🏁 Simple build test completed" -ForegroundColor Cyan
Write-Host "Exit code: $LASTEXITCODE" -ForegroundColor $(if ($LASTEXITCODE -eq 0) { "Green" } else { "Red" })