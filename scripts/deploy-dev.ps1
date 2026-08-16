# Deploy script for development environment
param(
    [string]$BuildArtifact
)

$ErrorActionPreference = "Stop"

Write-Host "Deploying to Development Environment"
Write-Host "Build Artifact: $BuildArtifact"
Write-Host "Git Commit: $env:CI_COMMIT_SHA"
Write-Host "Git Ref: $env:CI_COMMIT_REF_SLUG"

try {
    # Verify artifact exists
    if (-not (Test-Path $BuildArtifact)) {
        throw "Build artifact not found: $BuildArtifact"
    }

    # Add your deployment logic here
    # Example: Stop current service, backup, deploy, restart, health checks

    Write-Host "Development deployment completed successfully"
}
catch {
    Write-Error "Deployment failed: $_"
    exit 1
}
