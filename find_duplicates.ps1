# Find duplicate string resources in strings.xml
$content = Get-Content "app\src\main\res\values\strings.xml"
$duplicates = @{}

foreach ($line in $content) {
    if ($line -match 'name="([^"]+)"') {
        $name = $matches[1]
        if ($duplicates.ContainsKey($name)) {
            $duplicates[$name]++
        } else {
            $duplicates[$name] = 1
        }
    }
}

Write-Host "Duplicate string resources found:"
foreach ($key in $duplicates.Keys) {
    if ($duplicates[$key] -gt 1) {
        Write-Host "$key appears $($duplicates[$key]) times"
    }
}

