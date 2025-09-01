#!/usr/bin/env pwsh

# Test Build Without Room Processing
Write-Host "🧪 Testing Build Without Room Processing" -ForegroundColor Cyan
Write-Host "=======================================" -ForegroundColor Cyan

$ErrorActionPreference = "Continue"
Set-Location "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"

# Backup original build.gradle.kts
Write-Host "📋 Backing up app/build.gradle.kts..." -ForegroundColor Yellow
Copy-Item "app\build.gradle.kts" "app\build.gradle.kts.backup" -Force

# Create a simplified build.gradle.kts without Room
Write-Host "📝 Creating simplified build configuration..." -ForegroundColor Yellow

# Read the original file and comment out Room-related lines
$originalContent = Get-Content "app\build.gradle.kts" -Raw
$modifiedContent = $originalContent -replace 'id\("androidx\.room"\)', '// id("androidx.room")  // Temporarily disabled'
$modifiedContent = $modifiedContent -replace 'id\("com\.google\.devtools\.ksp"\)', '// id("com.google.devtools.ksp")  // Temporarily disabled'

# Comment out Room configuration
$modifiedContent = $modifiedContent -replace 'room \{[^}]*\}', '// room { schemaDirectory("$projectDir/schemas") }  // Temporarily disabled'

# Save the modified content
$modifiedContent | Out-File -FilePath "app\build.gradle.kts" -Encoding UTF8

Write-Host "✅ Simplified build configuration created" -ForegroundColor Green

# Try to build without Room
Write-Host "🔨 Attempting build without Room..." -ForegroundColor Yellow
try {
    $buildResult = & .\gradlew.bat "help" "--no-daemon" "--quiet" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Configuration successful without Room!" -ForegroundColor Green
        
        # Try to compile Kotlin sources
        Write-Host "🔨 Testing Kotlin compilation..." -ForegroundColor Yellow
        $compileResult = & .\gradlew.bat "compileDebugKotlin" "--no-daemon" "--quiet" 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Kotlin compilation successful!" -ForegroundColor Green
        } else {
            Write-Host "❌ Kotlin compilation failed" -ForegroundColor Red
            Write-Host "Error:" -ForegroundColor Red
            $compileResult | Select-Object -Last 10 | ForEach-Object { Write-Host $_ -ForegroundColor Red }
        }
    } else {
        Write-Host "❌ Configuration failed even without Room" -ForegroundColor Red
        Write-Host "Error:" -ForegroundColor Red
        $buildResult | Select-Object -Last 10 | ForEach-Object { Write-Host $_ -ForegroundColor Red }
    }
} catch {
    Write-Host "❌ Exception during build test: $($_.Exception.Message)" -ForegroundColor Red
}

# Restore original build.gradle.kts
Write-Host "🔄 Restoring original build.gradle.kts..." -ForegroundColor Yellow
Copy-Item "app\build.gradle.kts.backup" "app\build.gradle.kts" -Force
Remove-Item "app\build.gradle.kts.backup" -Force -ErrorAction SilentlyContinue

Write-Host "🏁 Test completed" -ForegroundColor Cyan
Write-Host "Result: $(if ($LASTEXITCODE -eq 0) { "SUCCESS" } else { "FAILED" })" -ForegroundColor $(if ($LASTEXITCODE -eq 0) { "Green" } else { "Red" })