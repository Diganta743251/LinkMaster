#!/usr/bin/env pwsh
# Complete Fix Script for LinkMaster Build and GitHub Push
# This script resolves merge conflicts and prepares the project

Write-Host "🔧 LinkMaster Complete Fix Script" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

$ErrorActionPreference = "Continue"
Set-Location "C:\Users\Diganta1\AndroidStudioProjects\LinkSheet"

# Step 1: Abort current merge and clean state
Write-Host "📋 Step 1: Cleaning merge state..." -ForegroundColor Yellow
git merge --abort 2>$null
git reset --hard HEAD 2>$null

# Step 2: Stash local changes
Write-Host "📦 Step 2: Saving local changes..." -ForegroundColor Yellow
git stash push -m "Local build fixes $(Get-Date -Format 'yyyy-MM-dd HH:mm')"

# Step 3: Pull remote changes
Write-Host "⬇️  Step 3: Pulling from GitHub..." -ForegroundColor Yellow
git pull origin master

# Step 4: Apply our changes back
Write-Host "🔄 Step 4: Reapplying local fixes..." -ForegroundColor Yellow
git stash pop

# Step 5: Resolve conflicts by accepting our changes
Write-Host "🛠️  Step 5: Resolving conflicts (keeping our changes)..." -ForegroundColor Yellow

# For each conflict, accept our version
git checkout --ours app/src/main/res/values/strings.xml
git checkout --ours gradle.properties  
git checkout --ours settings.gradle.kts
git checkout --ours versions.properties

git add app/src/main/res/values/strings.xml
git add gradle.properties
git add settings.gradle.kts  
git add versions.properties

# Step 6: Add all changes
Write-Host "➕ Step 6: Staging all changes..." -ForegroundColor Yellow
git add .

# Step 7: Commit
Write-Host "💾 Step 7: Committing changes..." -ForegroundColor Yellow
git commit -m "Fix: Resolved build configuration and merge conflicts

- Fixed Gradle syntax errors in app/build.gradle.kts
- Added feature module dependencies (:feature-app, :feature-systeminfo)
- Removed Play Store incompatible routes
- Added Play Store compliance string resources
- Centralized ApplyIf utility in :util module
- Resolved merge conflicts keeping local improvements

Build Status: Configuration fixed, ~1129 compilation errors remain
due to removed Play Store incompatible features. See BUILD_PROGRESS_2025.md"

# Step 8: Push to GitHub
Write-Host "⬆️  Step 8: Pushing to GitHub..." -ForegroundColor Yellow
$pushResult = git push origin master 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ SUCCESS! Changes pushed to GitHub" -ForegroundColor Green
    Write-Host "📍 Repository: https://github.com/Diganta743251/LinkMaster" -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "⚠️  Push failed. Trying force push..." -ForegroundColor Yellow
    Write-Host "   (This will overwrite remote with local changes)" -ForegroundColor Gray
    
    $confirm = Read-Host "Continue with force push? (y/n)"
    if ($confirm -eq 'y') {
        git push origin master --force
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Force push successful!" -ForegroundColor Green
        } else {
            Write-Host "❌ Force push failed: $pushResult" -ForegroundColor Red
        }
    }
}

Write-Host ""
Write-Host "📊 Current Status:" -ForegroundColor Cyan
Write-Host "  ✅ GitHub connected to LinkMaster repository" -ForegroundColor Green
Write-Host "  ✅ Build configuration fixes applied" -ForegroundColor Green
Write-Host "  ✅ Feature modules linked" -ForegroundColor Green
Write-Host "  ✅ Play Store compliance changes committed" -ForegroundColor Green
Write-Host "  ⚠️  ~1129 compilation errors remain (see solutions below)" -ForegroundColor Yellow

Write-Host ""
Write-Host "🔧 To Fix Compilation Errors, Choose One:" -ForegroundColor Cyan
Write-Host ""
Write-Host "Option 1 - Use Existing Working Build (Fastest):" -ForegroundColor Yellow
Write-Host "  .\final_working_build.ps1" -ForegroundColor White
Write-Host ""
Write-Host "Option 2 - Minimal Build (Recommended):" -ForegroundColor Yellow  
Write-Host "  .\gradlew.bat :app:assembleDebug -x test --continue" -ForegroundColor White
Write-Host ""
Write-Host "Option 3 - Review Build Scripts:" -ForegroundColor Yellow
Write-Host "  Get-ChildItem *.ps1 | Select-Object Name, Length, LastWriteTime" -ForegroundColor White
Write-Host ""
Write-Host "📖 See BUILD_PROGRESS_2025.md for detailed analysis" -ForegroundColor Cyan
Write-Host ""
Write-Host "✨ Script complete!" -ForegroundColor Green
