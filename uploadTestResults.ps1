Set-Location build/test-results/test

Write-Host "- About to upload test results"

$wc = New-Object "System.Net.WebClient"

Get-ChildItem -Path *.xml |
Foreach-Object {
    $wc.UploadFile("https://ci.appveyor.com/api/testresults/junit/$($env:APPVEYOR_JOB_ID)", (Resolve-Path $_.FullName))
}

Write-Host "- Results uploaded"
