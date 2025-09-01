#!/usr/bin/env pwsh

# Bypass Room Test - Temporarily disable Room to test other components
Write-Host "🧪 Bypass Room Test" -ForegroundColor Cyan
Write-Host "==================" -ForegroundColor Cyan

$ErrorActionPreference = "Continue"
Set-Location "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"

# Backup the original build.gradle.kts
Write-Host "📋 Backing up app/build.gradle.kts..." -ForegroundColor Yellow
Copy-Item "app\build.gradle.kts" "app\build.gradle.kts.room_backup" -Force

# Read and modify the build.gradle.kts to comment out Room and KSP
Write-Host "🔧 Temporarily disabling Room and KSP..." -ForegroundColor Yellow
$buildContent = Get-Content "app\build.gradle.kts" -Raw

# Comment out Room and KSP plugins
$buildContent = $buildContent -replace 'id\("androidx\.room"\)', '// id("androidx.room")  // Temporarily disabled'
$buildContent = $buildContent -replace 'id\("com\.google\.devtools\.ksp"\)', '// id("com.google.devtools.ksp")  // Temporarily disabled'

# Comment out Room configuration block
$buildContent = $buildContent -replace '(?s)room \{[^}]*\}', '// room { schemaDirectory("$projectDir/schemas") }  // Temporarily disabled'

# Save the modified content
$buildContent | Out-File -FilePath "app\build.gradle.kts" -Encoding UTF8

Write-Host "✅ Room and KSP temporarily disabled" -ForegroundColor Green

# Try to build without Room
Write-Host "🔨 Testing build without Room..." -ForegroundColor Yellow
try {
    $buildResult = & .\gradlew.bat "assembleFossDebug" "--no-daemon" "--stacktrace" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "🎉 BUILD SUCCESSFUL WITHOUT ROOM!" -ForegroundColor Green
        
        # Check for APK
        $apkFiles = Get-ChildItem -Path "app\build\outputs\apk" -Recurse -Filter "*.apk" -ErrorAction SilentlyContinue
        if ($apkFiles) {
            Write-Host "📱 APK files generated:" -ForegroundColor Green
            foreach ($apk in $apkFiles) {
                $sizeKB = [math]::Round($apk.Length / 1KB, 2)
                Write-Host "   📦 $($apk.Name) - ${sizeKB} KB" -ForegroundColor Cyan
            }
        }
        
        Write-Host "`n✅ CONCLUSION: The issue is specifically with Room/KSP processing" -ForegroundColor Green
        Write-Host "   Next step: Fix Room database queries or KSP configuration" -ForegroundColor Yellow
    } else {
        Write-Host "❌ Build failed even without Room" -ForegroundColor Red
        Write-Host "Last 10 lines of error:" -ForegroundColor Red
        $buildResult | Select-Object -Last 10 | ForEach-Object { Write-Host $_ -ForegroundColor Red }
        
        Write-Host "`n⚠️ CONCLUSION: There are other issues beyond Room" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ Exception during build: $($_.Exception.Message)" -ForegroundColor Red
}

# Restore original build.gradle.kts
Write-Host "`n🔄 Restoring original build.gradle.kts..." -ForegroundColor Yellow
Copy-Item "app\build.gradle.kts.room_backup" "app\build.gradle.kts" -Force
Remove-Item "app\build.gradle.kts.room_backup" -Force -ErrorAction SilentlyContinue

Write-Host "🏁 Bypass Room test completed" -ForegroundColor Cyan
Write-Host "Result: $(if ($LASTEXITCODE -eq 0) { "SUCCESS - Issue is Room-specific" } else { "FAILED - Multiple issues exist" })" -ForegroundColor $(if ($LASTEXITCODE -eq 0) { "Green" } else { "Red" })