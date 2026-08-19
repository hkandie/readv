param(
    [string]$ArtifactPath = "backend\build\libs"
)

$ErrorActionPreference = "Stop"

try {
    Write-Output "Starting Veracode Pipeline Scan..."

    $scanDir = "veracode-pipeline-scan"
    New-Item -ItemType Directory -Force -Path $scanDir | Out-Null

    Invoke-WebRequest -Uri "https://downloads.veracode.com/securityscan/pipeline-scan-LATEST.zip" -OutFile "$scanDir\pipeline-scan.zip"
    Expand-Archive -Path "$scanDir\pipeline-scan.zip" -DestinationPath $scanDir -Force

    $artifactJar = Get-ChildItem -Path $ArtifactPath -Filter "*.jar" |
        Where-Object { $_.Name -notlike "*-plain.jar" } |
        Select-Object -First 1
    if (-not $artifactJar) {
        throw "No .jar file found in '$ArtifactPath'"
    }

    java -jar "$scanDir\pipeline-scan.jar" `
        -vid $env:VERACODE_API_ID `
        -vkey $env:VERACODE_API_SECRET `
        -f "$($artifactJar.FullName)" `
        --fail_on_severity="Very High, High" `
        -gvg true

    if ($LASTEXITCODE -ne 0) {
        Write-Error "Veracode Pipeline Scan failed with exit code $LASTEXITCODE"
        exit $LASTEXITCODE
    }

    Write-Output "Veracode Pipeline Scan completed successfully."
}
catch {
    Write-Error "Veracode Pipeline Scan failed: $_"
    exit 1
}
