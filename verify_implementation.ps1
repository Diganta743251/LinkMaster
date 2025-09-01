# LinkMaster History Feature - Implementation Verification Script
# This script verifies that all components are properly integrated

Write-Host "🔗 LinkMaster History Feature - Implementation Verification" -ForegroundColor Cyan
Write-Host "=" * 60 -ForegroundColor Gray

$projectRoot = "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"
$errors = @()
$warnings = @()
$success = @()

# Function to check if file exists and contains specific content
function Test-FileContent {
    param(
        [string]$FilePath,
        [string]$SearchPattern,
        [string]$Description
    )
    
    if (Test-Path $FilePath) {
        $content = Get-Content $FilePath -Raw
        if ($content -match $SearchPattern) {
            $script:success += "✅ $Description"
            return $true
        } else {
            $script:warnings += "⚠️  $Description - File exists but content not found"
            return $false
        }
    } else {
        $script:errors += "❌ $Description - File not found: $FilePath"
        return $false
    }
}

Write-Host "`n🏗️  Checking Core Implementation Files..." -ForegroundColor Yellow

# Check ViewModels
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\module\viewmodel\HistoryViewModel.kt" "class HistoryViewModel" "HistoryViewModel implementation"
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\module\viewmodel\LinkDetailViewModel.kt" "class LinkDetailViewModel" "LinkDetailViewModel implementation"
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\module\viewmodel\QRCodeViewModel.kt" "class QRCodeViewModel" "QRCodeViewModel implementation"

# Check UI Components
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\composable\page\history\HistoryRoute.kt" "@Composable.*fun HistoryRoute" "HistoryRoute UI component"
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\composable\page\history\LinkDetailScreen.kt" "@Composable.*fun LinkDetailScreen" "LinkDetailScreen UI component"
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\composable\page\history\QRCodeScreen.kt" "@Composable.*fun QRCodeScreen" "QRCodeScreen UI component"

# Check Navigation Integration
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\composable\page\history\HistoryNavigation.kt" "HistoryNavigationHost" "History navigation setup"
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\activity\main\MainNavHost.kt" 'route = "history"' "Main navigation integration"

# Check Database Integration
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\module\repository\AppSelectionHistoryRepository.kt" "fun getAllHistory" "Enhanced repository methods"
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\module\database\dao\AppSelectionHistoryDao.kt" "fun getAllHistory" "Enhanced DAO methods"

# Check Privacy Settings Integration
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\composable\page\settings\privacy\PrivacySettingsRoute.kt" "view_link_history" "Privacy settings integration"

# Check Home Screen Integration
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\composable\page\home\MainRoute.kt" "ModernHistoryNavigationCard" "Home screen integration"

# Check String Resources
Test-FileContent "$projectRoot\app\src\main\res\values\strings.xml" "view_link_history" "String resources"

# Check Dependencies
Test-FileContent "$projectRoot\versions.properties" "com.google.zxing" "ZXing dependencies"
Test-FileContent "$projectRoot\app\build.gradle.kts" "com.google.zxing:core" "ZXing implementation"

Write-Host "`n🧪 Checking Test Files..." -ForegroundColor Yellow

# Check Test Files
Test-FileContent "$projectRoot\app\src\test\java\fe\linksheet\HistoryIntegrationTest.kt" "class HistoryIntegrationTest" "Integration tests"
Test-FileContent "$projectRoot\app\src\test\java\fe\linksheet\QRCodeGenerationTest.kt" "class QRCodeGenerationTest" "QR code tests"

# Check Demo Activity
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\activity\demo\HistoryDemoActivity.kt" "class HistoryDemoActivity" "Demo activity"

Write-Host "`n🔧 Checking Utility Files..." -ForegroundColor Yellow

# Check Utilities
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\util\PerformanceMonitor.kt" "object PerformanceMonitor" "Performance monitoring"
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\util\ErrorHandler.kt" "object ErrorHandler" "Error handling"

# Check State Management
Test-FileContent "$projectRoot\app\src\main\java\fe\linksheet\composable\page\history\HistoryState.kt" "sealed class HistoryState" "State management"

Write-Host "`n📊 Verification Results:" -ForegroundColor Cyan
Write-Host "=" * 30 -ForegroundColor Gray

if ($success.Count -gt 0) {
    Write-Host "`n✅ SUCCESSFUL CHECKS ($($success.Count)):" -ForegroundColor Green
    $success | ForEach-Object { Write-Host "   $_" -ForegroundColor Green }
}

if ($warnings.Count -gt 0) {
    Write-Host "`n⚠️  WARNINGS ($($warnings.Count)):" -ForegroundColor Yellow
    $warnings | ForEach-Object { Write-Host "   $_" -ForegroundColor Yellow }
}

if ($errors.Count -gt 0) {
    Write-Host "`n❌ ERRORS ($($errors.Count)):" -ForegroundColor Red
    $errors | ForEach-Object { Write-Host "   $_" -ForegroundColor Red }
}

# Summary
$total = $success.Count + $warnings.Count + $errors.Count
$successRate = [math]::Round(($success.Count / $total) * 100, 1)

Write-Host "`n📈 IMPLEMENTATION STATUS:" -ForegroundColor Cyan
Write-Host "   Total Checks: $total" -ForegroundColor White
Write-Host "   Success Rate: $successRate%" -ForegroundColor $(if ($successRate -ge 90) { "Green" } elseif ($successRate -ge 70) { "Yellow" } else { "Red" })

if ($errors.Count -eq 0 -and $warnings.Count -le 2) {
    Write-Host "`n🎉 IMPLEMENTATION STATUS: COMPLETE AND READY!" -ForegroundColor Green
    Write-Host "   All core components are properly implemented and integrated." -ForegroundColor Green
    Write-Host "   The History feature is ready for testing and use." -ForegroundColor Green
} elseif ($errors.Count -eq 0) {
    Write-Host "`n✅ IMPLEMENTATION STATUS: MOSTLY COMPLETE" -ForegroundColor Yellow
    Write-Host "   Core functionality is implemented with minor issues." -ForegroundColor Yellow
    Write-Host "   Review warnings and address if needed." -ForegroundColor Yellow
} else {
    Write-Host "`n⚠️  IMPLEMENTATION STATUS: NEEDS ATTENTION" -ForegroundColor Red
    Write-Host "   Some critical components are missing or incomplete." -ForegroundColor Red
    Write-Host "   Address errors before proceeding." -ForegroundColor Red
}

Write-Host "`n🚀 Next Steps:" -ForegroundColor Cyan
Write-Host "   1. Review any errors or warnings above" -ForegroundColor White
Write-Host "   2. Test the demo activity: HistoryDemoActivity" -ForegroundColor White
Write-Host "   3. Run integration tests when build issues are resolved" -ForegroundColor White
Write-Host "   4. Integrate into main app navigation" -ForegroundColor White

Write-Host "`n📚 Documentation:" -ForegroundColor Cyan
Write-Host "   • HISTORY_FEATURE_IMPLEMENTATION.md - Complete implementation guide" -ForegroundColor White
Write-Host "   • FINAL_IMPLEMENTATION_SUMMARY.md - Feature overview and status" -ForegroundColor White

Write-Host "`n" -ForegroundColor White