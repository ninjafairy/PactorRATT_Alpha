# Package the shaded jar and copy it to Builds\Most Recent Build.
# exec.skip leaves build.number.properties unchanged. The jar still records the current number.
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

$mvn = Join-Path $PSScriptRoot ".tools\apache-maven-3.9.6\bin\mvn.cmd"
if (-not (Test-Path $mvn)) {
    Write-Error "Maven not found at $mvn"
}

& $mvn -q package "-Dexec.skip=true"
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

$jar = Join-Path $PSScriptRoot "target\PactorRATT_Alpha.jar"
if (-not (Test-Path $jar)) {
    Write-Error "Packaged jar not found at $jar"
}

$destDir = Join-Path $PSScriptRoot "Builds\Most Recent Build"
New-Item -ItemType Directory -Force -Path $destDir | Out-Null
$dest = Join-Path $destDir "PactorRATT_Alpha.jar"
Copy-Item -Force $jar $dest

$buildFile = Join-Path $PSScriptRoot "build.number.properties"
$buildNumber = (Select-String -Path $buildFile -Pattern '^buildNumber=(\d+)').Matches[0].Groups[1].Value
Write-Host "Build $buildNumber packaged to $dest"
exit 0
