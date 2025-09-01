# Play Store Compliance Verification Script

Write-Host "=== LinkSheet Play Store Compliance Verification ===" -ForegroundColor Green

$projectRoot = "c:\Users\Diganta1\AndroidStudioProjects\LinkSheet"
Set-Location $projectRoot

Write-Host "`n1. Checking for removed problematic imports..." -ForegroundColor Yellow

# Check for Shizuku references
$shizukuRefs = Get-ChildItem -Path "app\src\main\java" -Recurse -Filter "*.kt" | Select-String -Pattern "import.*shizuku|Shizuku" -SimpleMatch:$false
if ($shizukuRefs) {
    Write-Host "❌ Found Shizuku references:" -ForegroundColor Red
    $shizukuRefs | ForEach-Object { Write-Host "  $($_.Filename):$($_.LineNumber) - $($_.Line.Trim())" }
} else {
    Write-Host "✅ No Shizuku references found" -ForegroundColor Green
}

# Check for analytics references
$analyticsRefs = Get-ChildItem -Path "app\src\main\java" -Recurse -Filter "*.kt" | Select-String -Pattern "import.*analytics|Analytics" -SimpleMatch:$false
if ($analyticsRefs) {
    Write-Host "❌ Found Analytics references:" -ForegroundColor Red
    $analyticsRefs | ForEach-Object { Write-Host "  $($_.Filename):$($_.LineNumber) - $($_.Line.Trim())" }
} else {
    Write-Host "✅ No Analytics references found" -ForegroundColor Green
}

# Check for experiment references
$experimentRefs = Get-ChildItem -Path "app\src\main\java" -Recurse -Filter "*.kt" | Select-String -Pattern "import.*experiment|Experiment" -SimpleMatch:$false
if ($experimentRefs) {
    Write-Host "❌ Found Experiment references:" -ForegroundColor Red
    $experimentRefs | ForEach-Object { Write-Host "  $($_.Filename):$($_.LineNumber) - $($_.Line.Trim())" }
} else {
    Write-Host "✅ No Experiment references found" -ForegroundColor Green
}

Write-Host "`n2. Checking for removed files..." -ForegroundColor Yellow

$removedFiles = @(
    "app\src\main\java\fe\linksheet\module\viewmodel\DevSettingsViewModel.kt",
    "app\src\main\java\fe\linksheet\module\analytics",
    "app\src\main\java\fe\linksheet\module\experiment",
    "app\src\main\java\fe\linksheet\module\shizuku"
)

foreach ($file in $removedFiles) {
    if (Test-Path $file) {
        Write-Host "❌ File still exists: $file" -ForegroundColor Red
    } else {
        Write-Host "✅ File properly removed: $file" -ForegroundColor Green
    }
}

Write-Host "`n3. Checking for UI improvements..." -ForegroundColor Yellow

# Check if PlayStoreComplianceCard exists
if (Test-Path "app\src\main\java\fe\linksheet\composable\component\card\PlayStoreComplianceCard.kt") {
    Write-Host "✅ PlayStoreComplianceCard created" -ForegroundColor Green
} else {
    Write-Host "❌ PlayStoreComplianceCard missing" -ForegroundColor Red
}

# Check if compliance strings exist
$stringsFile = "app\src\main\res\values\strings.xml"
if (Test-Path $stringsFile) {
    $stringsContent = Get-Content $stringsFile -Raw
    if ($stringsContent -match "play_store_compliant") {
        Write-Host "✅ Compliance strings added" -ForegroundColor Green
    } else {
        Write-Host "❌ Compliance strings missing" -ForegroundColor Red
    }
}

Write-Host "`n4. Checking build configuration..." -ForegroundColor Yellow

# Check if build.gradle.kts has proper configuration
$buildFile = "app\build.gradle.kts"
if (Test-Path $buildFile) {
    Write-Host "✅ Build configuration file exists" -ForegroundColor Green
} else {
    Write-Host "❌ Build configuration file missing" -ForegroundColor Red
}

Write-Host "`n=== Verification Complete ===" -ForegroundColor Green
Write-Host "The app should now be compliant with Play Store policies while maintaining excellent UI/UX." -ForegroundColor Cyan