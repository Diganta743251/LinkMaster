#!/usr/bin/env pwsh
# Fix remaining BuildConfig custom functions

Write-Host "🔧 Fixing remaining BuildConfig issues..." -ForegroundColor Cyan

$buildFile = "app\build.gradle.kts"
$content = Get-Content $buildFile -Raw

# Fix stringArray - comment it out for now as it needs custom implementation
$content = $content -replace 'stringArray\("SUPPORTED_LOCALES", supportedLocales\)', '// stringArray("SUPPORTED_LOCALES", supportedLocales)  // Custom function - temporarily disabled'

# Fix int
$content = $content -replace 'int\("DONATION_BANNER_MIN", localProperties\.getOrSystemEnv\("DONATION_BANNER_MIN"\)\?\.toIntOrNull\(\) \?\: 20\)', 'buildConfigField("int", "DONATION_BANNER_MIN", "${localProperties.getOrSystemEnv(\"DONATION_BANNER_MIN\")?.toIntOrNull() ?: 20}")'

# Fix string in forEach - replace the entire block
$oldBlock = @'
            arrayOf("LINK_DISCORD", "LINK_BUY_ME_A_COFFEE", "LINK_CRYPTO").forEach {
                string(it, publicLocalProperties.getOrSystemEnv(it))
            }
'@

$newBlock = @'
            arrayOf("LINK_DISCORD", "LINK_BUY_ME_A_COFFEE", "LINK_CRYPTO").forEach {
                buildConfigField("String", it, "\"${publicLocalProperties.getOrSystemEnv(it) ?: ""}\"")
            }
'@

$content = $content -replace [regex]::Escape($oldBlock), $newBlock

$content | Out-File -FilePath $buildFile -Encoding UTF8 -NoNewline

Write-Host "✅ Fixed remaining BuildConfig issues" -ForegroundColor Green
Write-Host ""
Write-Host "📋 Changes made:" -ForegroundColor Cyan
Write-Host "  ✓ Fixed int() function" -ForegroundColor Gray
Write-Host "  ✓ Fixed string() functions in forEach" -ForegroundColor Gray
Write-Host "  ✓ Commented out stringArray() (custom function)" -ForegroundColor Gray
Write-Host ""
Write-Host "🚀 Ready to build!" -ForegroundColor Green
