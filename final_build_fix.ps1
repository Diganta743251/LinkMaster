#!/usr/bin/env pwsh

# Final Comprehensive Build Fix
Write-Host "🎯 Final Comprehensive Build Fix" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan

$ErrorActionPreference = "Continue"
Set-Location "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"

# Step 1: Clean everything
Write-Host "`n🧹 Step 1: Complete cleanup..." -ForegroundColor Yellow
.\gradlew --stop 2>&1 | Out-Null
Remove-Item -Path "build" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "app\build" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path ".gradle" -Recurse -Force -ErrorAction SilentlyContinue
Write-Host "✅ Cleanup completed" -ForegroundColor Green

# Step 2: Enable KSP2 which might be more stable
Write-Host "`n🔧 Step 2: Enabling KSP2 for better stability..." -ForegroundColor Yellow
$gradleProps = Get-Content "gradle.properties" -Raw
$gradleProps = $gradleProps -replace "# # useKSP2=true.*", "useKSP2=true"
$gradleProps | Out-File -FilePath "gradle.properties" -Encoding UTF8
Write-Host "✅ KSP2 enabled" -ForegroundColor Green

# Step 3: Optimize JVM settings for KSP
Write-Host "`n⚙️ Step 3: Optimizing JVM settings for KSP..." -ForegroundColor Yellow
$gradleProps = Get-Content "gradle.properties" -Raw
$gradleProps = $gradleProps -replace "org\.gradle\.jvmargs=-Xmx6g", "org.gradle.jvmargs=-Xmx8g"
$gradleProps = $gradleProps -replace "kotlin\.daemon\.jvmargs=-Xmx4G", "kotlin.daemon.jvmargs=-Xmx6G"
$gradleProps | Out-File -FilePath "gradle.properties" -Encoding UTF8
Write-Host "✅ JVM settings optimized" -ForegroundColor Green

# Step 4: Add KSP-specific JVM arguments
Write-Host "`n🔧 Step 4: Adding KSP-specific optimizations..." -ForegroundColor Yellow
$gradleProps = Get-Content "gradle.properties" -Raw
if ($gradleProps -notmatch "ksp\.incremental") {
    $gradleProps += "`n# KSP optimizations`nksp.incremental=true`nksp.incremental.intermodule=true`n"
    $gradleProps | Out-File -FilePath "gradle.properties" -Encoding UTF8
}
Write-Host "✅ KSP optimizations added" -ForegroundColor Green

# Step 5: Try the build
Write-Host "`n🔨 Step 5: Attempting build with optimizations..." -ForegroundColor Yellow
$buildStart = Get-Date
try {
    $buildOutput = & .\gradlew.bat "assembleFossDebug" "--no-daemon" "--stacktrace" 2>&1
    $buildEnd = Get-Date
    $buildTime = ($buildEnd - $buildStart).TotalMinutes
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "🎉 BUILD SUCCESSFUL!" -ForegroundColor Green
        Write-Host "⏱️ Build time: $([math]::Round($buildTime, 1)) minutes" -ForegroundColor Cyan
        
        # Check for APK files
        $apkFiles = Get-ChildItem -Path "app\build\outputs\apk" -Recurse -Filter "*.apk" -ErrorAction SilentlyContinue
        if ($apkFiles) {
            Write-Host "`n📱 Generated APK files:" -ForegroundColor Green
            foreach ($apk in $apkFiles) {
                $sizeMB = [math]::Round($apk.Length / 1MB, 2)
                Write-Host "   📦 $($apk.Name) - ${sizeMB} MB" -ForegroundColor Cyan
                Write-Host "   📍 Location: $($apk.FullName)" -ForegroundColor Gray
            }
        }
        
        Write-Host "`n🎯 SUCCESS SUMMARY:" -ForegroundColor Green
        Write-Host "   ✅ All build issues resolved" -ForegroundColor Green
        Write-Host "   ✅ Room database compilation successful" -ForegroundColor Green
        Write-Host "   ✅ KSP processing completed" -ForegroundColor Green
        Write-Host "   ✅ APK generated successfully" -ForegroundColor Green
        
    } else {
        Write-Host "❌ Build still failed" -ForegroundColor Red
        Write-Host "⏱️ Failed after: $([math]::Round($buildTime, 1)) minutes" -ForegroundColor Red
        
        # Analyze the error
        $errorLines = $buildOutput | Where-Object { $_ -match "error|Error|ERROR|failed|Failed|FAILED" } | Select-Object -Last 5
        Write-Host "`n🔍 Key error messages:" -ForegroundColor Red
        foreach ($error in $errorLines) {
            Write-Host "   ❌ $error" -ForegroundColor Red
        }
        
        # Check if it's still a KSP issue
        if ($buildOutput -match "ksp|KSP|symbol.*processing") {
            Write-Host "`n💡 RECOMMENDATION: KSP issues persist" -ForegroundColor Yellow
            Write-Host "   Try: Downgrade Kotlin to stable version (2.1.0)" -ForegroundColor Yellow
            Write-Host "   Or: Use Room without KSP (kapt instead)" -ForegroundColor Yellow
        }
        
        # Check if it's a Room issue
        if ($buildOutput -match "room|Room|ROOM|database") {
            Write-Host "`n💡 RECOMMENDATION: Room database issues" -ForegroundColor Yellow
            Write-Host "   Check: Database entity definitions" -ForegroundColor Yellow
            Write-Host "   Check: DAO query syntax" -ForegroundColor Yellow
        }
    }
} catch {
    Write-Host "❌ Exception during build: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🏁 Final build fix completed" -ForegroundColor Cyan
Write-Host "Exit code: $LASTEXITCODE" -ForegroundColor $(if ($LASTEXITCODE -eq 0) { "Green" } else { "Red" })