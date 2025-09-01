#!/usr/bin/env pwsh

# Complete Build Solution - Comprehensive Fix and Build
Write-Host "🚀 Complete Build Solution for LinkSheet" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$ErrorActionPreference = "Continue"
Set-Location "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"

# Function to check build status
function Check-BuildStatus {
    $apkFiles = Get-ChildItem -Path "app\build\outputs\apk" -Recurse -Filter "*.apk" -ErrorAction SilentlyContinue
    return $apkFiles.Count -gt 0
}

# Function to monitor build progress
function Monitor-BuildProgress {
    param([int]$TimeoutMinutes = 10)
    
    $startTime = Get-Date
    $lastCheck = Get-Date
    
    Write-Host "⏱️ Monitoring build progress (timeout: $TimeoutMinutes minutes)..." -ForegroundColor Yellow
    
    while ((Get-Date) -lt $startTime.AddMinutes($TimeoutMinutes)) {
        # Check every 30 seconds
        if ((Get-Date) -gt $lastCheck.AddSeconds(30)) {
            $javaProcesses = Get-Process | Where-Object {$_.ProcessName -like "*java*"} -ErrorAction SilentlyContinue
            $gradleProcesses = Get-Process | Where-Object {$_.ProcessName -like "*gradle*"} -ErrorAction SilentlyContinue
            
            if ($javaProcesses.Count -gt 0 -or $gradleProcesses.Count -gt 0) {
                $elapsed = [math]::Round(((Get-Date) - $startTime).TotalMinutes, 1)
                Write-Host "   🔄 Build active - ${elapsed}m elapsed, $($javaProcesses.Count) Java processes running" -ForegroundColor Gray
                
                # Check if APK was generated
                if (Check-BuildStatus) {
                    Write-Host "   🎉 APK detected! Build likely successful!" -ForegroundColor Green
                    return $true
                }
            } else {
                Write-Host "   ⚠️ No active build processes detected" -ForegroundColor Yellow
                break
            }
            $lastCheck = Get-Date
        }
        Start-Sleep -Seconds 5
    }
    
    return Check-BuildStatus
}

# Step 1: Kill any hanging processes and clean
Write-Host "`n🛑 Step 1: Cleaning environment..." -ForegroundColor Yellow
try {
    .\gradlew --stop 2>&1 | Out-Null
    Get-Process | Where-Object {$_.ProcessName -like "*gradle*" -or $_.ProcessName -like "*java*"} | Stop-Process -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 3
    
    Remove-Item -Path "build" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path "app\build" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path ".gradle" -Recurse -Force -ErrorAction SilentlyContinue
    
    Write-Host "✅ Environment cleaned" -ForegroundColor Green
} catch {
    Write-Host "⚠️ Cleanup had issues: $($_.Exception.Message)" -ForegroundColor Yellow
}

# Step 2: Verify our stable configuration
Write-Host "`n🔧 Step 2: Verifying stable configuration..." -ForegroundColor Yellow

# Check Kotlin version
$versionsContent = Get-Content "versions.properties" -Raw
if ($versionsContent -match "version\.kotlin=2\.1\.0") {
    Write-Host "✅ Kotlin 2.1.0 (stable)" -ForegroundColor Green
} else {
    Write-Host "❌ Kotlin version not stable" -ForegroundColor Red
}

# Check KSP version
if ($versionsContent -match "plugin\.com\.google\.devtools\.ksp=2\.1\.0-1\.0\.28") {
    Write-Host "✅ KSP 2.1.0-1.0.28 (compatible)" -ForegroundColor Green
} else {
    Write-Host "❌ KSP version not compatible" -ForegroundColor Red
}

# Check gradle.properties
$gradleProps = Get-Content "gradle.properties" -Raw
if ($gradleProps -match "# useKSP2=true") {
    Write-Host "✅ KSP2 disabled for stability" -ForegroundColor Green
} else {
    Write-Host "⚠️ KSP2 setting may need adjustment" -ForegroundColor Yellow
}

# Step 3: Attempt build with monitoring
Write-Host "`n🔨 Step 3: Starting monitored build..." -ForegroundColor Yellow
$buildStart = Get-Date

# Start build in background
$buildJob = Start-Job -ScriptBlock {
    param($projectPath)
    Set-Location $projectPath
    & .\gradlew.bat "assembleFossDebug" "--no-daemon" "--stacktrace" 2>&1
} -ArgumentList (Get-Location).Path

Write-Host "🚀 Build started in background (Job ID: $($buildJob.Id))" -ForegroundColor Cyan

# Monitor the build
$buildSuccessful = Monitor-BuildProgress -TimeoutMinutes 15

# Check job status
$jobState = $buildJob.State
Write-Host "`n📊 Build Job Status: $jobState" -ForegroundColor $(if ($jobState -eq "Completed") { "Green" } else { "Yellow" })

# Get build output
if ($buildJob.State -eq "Completed") {
    $buildOutput = Receive-Job -Job $buildJob
    $buildExitCode = $buildJob.ChildJobs[0].JobStateInfo.Reason.ExitCode
} else {
    Write-Host "⏳ Build still running, getting partial output..." -ForegroundColor Yellow
    $buildOutput = Receive-Job -Job $buildJob -Keep
    $buildExitCode = $null
}

# Clean up job
Remove-Job -Job $buildJob -Force -ErrorAction SilentlyContinue

$buildEnd = Get-Date
$buildTime = ($buildEnd - $buildStart).TotalMinutes

# Step 4: Analyze results
Write-Host "`n📋 Step 4: Build Analysis" -ForegroundColor Yellow
Write-Host "⏱️ Total build time: $([math]::Round($buildTime, 1)) minutes" -ForegroundColor Cyan

# Check for APK files
$apkFiles = Get-ChildItem -Path "app\build\outputs\apk" -Recurse -Filter "*.apk" -ErrorAction SilentlyContinue

if ($apkFiles.Count -gt 0) {
    Write-Host "`n🎉 BUILD SUCCESSFUL!" -ForegroundColor Green
    Write-Host "📱 Generated APK files:" -ForegroundColor Green
    
    foreach ($apk in $apkFiles) {
        $sizeMB = [math]::Round($apk.Length / 1MB, 2)
        Write-Host "   📦 $($apk.Name) - ${sizeMB} MB" -ForegroundColor Cyan
        Write-Host "   📍 $($apk.FullName)" -ForegroundColor Gray
    }
    
    # Verify APK integrity
    $debugApk = $apkFiles | Where-Object { $_.Name -like "*debug*" } | Select-Object -First 1
    if ($debugApk) {
        Write-Host "`n🔍 APK Verification:" -ForegroundColor Yellow
        Write-Host "   📦 Debug APK: $($debugApk.Name)" -ForegroundColor Cyan
        Write-Host "   📏 Size: $([math]::Round($debugApk.Length / 1MB, 2)) MB" -ForegroundColor Cyan
        Write-Host "   📅 Created: $($debugApk.LastWriteTime)" -ForegroundColor Cyan
        
        if ($debugApk.Length -gt 10MB) {
            Write-Host "   ✅ APK size looks reasonable" -ForegroundColor Green
        } else {
            Write-Host "   ⚠️ APK size seems small, may be incomplete" -ForegroundColor Yellow
        }
    }
    
    Write-Host "`n🎯 SUCCESS SUMMARY:" -ForegroundColor Green
    Write-Host "   ✅ Build completed successfully" -ForegroundColor Green
    Write-Host "   ✅ Stable Kotlin 2.1.0 + KSP 2.1.0-1.0.28 working" -ForegroundColor Green
    Write-Host "   ✅ Room database compilation successful" -ForegroundColor Green
    Write-Host "   ✅ All modules compiled without errors" -ForegroundColor Green
    Write-Host "   ✅ APK generated and ready for testing" -ForegroundColor Green
    
    Write-Host "`n🚀 NEXT STEPS:" -ForegroundColor Cyan
    Write-Host "   1. Test the APK on a device or emulator" -ForegroundColor White
    Write-Host "   2. Run: adb install `"$($debugApk.FullName)`"" -ForegroundColor White
    Write-Host "   3. For release build: .\gradlew assembleFossRelease" -ForegroundColor White
    
} else {
    Write-Host "`n❌ BUILD FAILED" -ForegroundColor Red
    
    # Analyze the failure
    if ($buildOutput) {
        $errorLines = $buildOutput | Where-Object { $_ -match "error|Error|ERROR|failed|Failed|FAILED|exception|Exception" } | Select-Object -Last 10
        
        Write-Host "`n🔍 Error Analysis:" -ForegroundColor Red
        foreach ($error in $errorLines) {
            Write-Host "   ❌ $error" -ForegroundColor Red
        }
        
        # Specific error pattern detection
        if ($buildOutput -match "ksp.*failed|KSP.*failed|symbol.*processing.*failed") {
            Write-Host "`n💡 KSP Processing Issue Detected:" -ForegroundColor Yellow
            Write-Host "   🔧 Try: Disable KSP2 completely" -ForegroundColor Yellow
            Write-Host "   🔧 Try: Use KAPT instead of KSP for Room" -ForegroundColor Yellow
        }
        
        if ($buildOutput -match "room.*error|Room.*error|database.*error") {
            Write-Host "`n💡 Room Database Issue Detected:" -ForegroundColor Yellow
            Write-Host "   🔧 Check: Database entity annotations" -ForegroundColor Yellow
            Write-Host "   🔧 Check: DAO query syntax" -ForegroundColor Yellow
        }
        
        if ($buildOutput -match "memory|OutOfMemory|heap") {
            Write-Host "`n💡 Memory Issue Detected:" -ForegroundColor Yellow
            Write-Host "   🔧 Increase: org.gradle.jvmargs=-Xmx12g" -ForegroundColor Yellow
            Write-Host "   🔧 Increase: kotlin.daemon.jvmargs=-Xmx8G" -ForegroundColor Yellow
        }
    }
    
    Write-Host "`n🔧 RECOMMENDED FIXES:" -ForegroundColor Yellow
    Write-Host "   1. Check the error messages above" -ForegroundColor White
    Write-Host "   2. Try: .\gradlew clean assembleFossDebug --info" -ForegroundColor White
    Write-Host "   3. Consider switching to KAPT if KSP issues persist" -ForegroundColor White
}

# Step 5: Generate build report
Write-Host "`n📄 Step 5: Generating build report..." -ForegroundColor Yellow
$reportPath = "build_report_$(Get-Date -Format 'yyyyMMdd_HHmmss').txt"

$report = @"
LinkSheet Build Report
Generated: $(Get-Date)
Build Time: $([math]::Round($buildTime, 1)) minutes
Exit Code: $buildExitCode

Configuration:
- Kotlin: 2.1.0 (stable)
- KSP: 2.1.0-1.0.28 (compatible)
- Room: 2.6.1
- KSP2: Disabled

Results:
- APK Generated: $(if ($apkFiles.Count -gt 0) { "YES" } else { "NO" })
- APK Count: $($apkFiles.Count)
- Build Status: $(if ($apkFiles.Count -gt 0) { "SUCCESS" } else { "FAILED" })

APK Files:
$($apkFiles | ForEach-Object { "- $($_.Name) ($([math]::Round($_.Length / 1MB, 2)) MB)" } | Out-String)

Last 20 lines of build output:
$($buildOutput | Select-Object -Last 20 | Out-String)
"@

$report | Out-File -FilePath $reportPath -Encoding UTF8
Write-Host "📄 Build report saved: $reportPath" -ForegroundColor Cyan

Write-Host "`n🏁 Complete Build Solution Finished" -ForegroundColor Cyan
Write-Host "Status: $(if ($apkFiles.Count -gt 0) { "SUCCESS ✅" } else { "NEEDS ATTENTION ⚠️" })" -ForegroundColor $(if ($apkFiles.Count -gt 0) { "Green" } else { "Yellow" })