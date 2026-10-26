# Launch the compiled app from target\classes. Does not package or bump the build number.
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

$mvn = Join-Path $PSScriptRoot ".tools\apache-maven-3.9.6\bin\mvn.cmd"
if (-not (Test-Path $mvn)) {
    Write-Error "Maven not found at $mvn"
}

& $mvn -q compiler:compile exec:java "-Dexec.mainClass=com.pactorratt.alpha.app.PactorRattAlphaApp"
exit $LASTEXITCODE
