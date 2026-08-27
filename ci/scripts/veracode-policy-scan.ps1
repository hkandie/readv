param(
    [string]$VeracodeAccountId = "62667"
)

$ErrorActionPreference = "Stop"

try {
    $veracode_app_name = $env:veracode_app_name
    $veracode_sandbox_name = $env:veracode_sandbox_name
    $javaExe = "$env:JAVA_HOME\bin\java.exe"

    Write-Output (Get-Location)

    $latest_version = (Invoke-WebRequest -Uri 'https://search.maven.org/solrsearch/select?q=g:com.veracode.vosp.api.wrappers+AND+a:vosp-api-wrappers-java&start=0&rows=1' | ConvertFrom-Json).response.docs[0].latestVersion
    if (-not $latest_version) {
        throw "Could not resolve latest vosp-api-wrappers-java version from Maven Central"
    }

    if (Test-Path ./veracode_version.txt) {
        $current_version = Get-Content ./veracode_version.txt
    } else {
        $current_version = ""
    }

    if ($latest_version -ne $current_version -or -not (Test-Path ./VeracodeJavaAPI.jar)) {
        Write-Host "Current Version: $current_version Newest Version Available: $latest_version"
        Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/com/veracode/vosp/api/wrappers/vosp-api-wrappers-java/$latest_version/vosp-api-wrappers-java-$latest_version.jar" -OutFile ./VeracodeJavaAPI.jar
        $latest_version | Set-Content ./veracode_version.txt
    } else {
        Write-Host "Veracode version is up to date"
    }
    Write-Host "Using Version $latest_version"

    if (-not (Test-Path ./VeracodeJavaAPI.jar)) {
        throw "VeracodeJavaAPI.jar was not downloaded"
    }

    $backendJar = Get-ChildItem -Path "backend/build/libs" -Filter "*.jar" -ErrorAction SilentlyContinue |
        Where-Object { $_.Name -notlike "*-plain.jar" } |
        Select-Object -First 1
    if (-not $backendJar) {
        throw "No backend jar found in backend/build/libs"
    }

    $webJar = Get-ChildItem -Path "static-host/build/libs" -Filter "*.jar" -ErrorAction SilentlyContinue |
        Where-Object { $_.Name -notlike "*-plain.jar" } |
        Select-Object -First 1
    if (-not $webJar) {
        throw "No web jar found in static-host/build/libs"
    }

    $file_upload_args = @("-filepath", $backendJar.FullName, "-filepath", $webJar.FullName)
    if (Test-Path "backend/backend-src.zip") {
        $file_upload_args += @("-filepath", "backend/backend-src.zip")
    }
    if (Test-Path "static-host/static-host-src.zip") {
        $file_upload_args += @("-filepath", "static-host/static-host-src.zip")
    }
    if (Test-Path "frontend/front-end-src.zip") {
        $file_upload_args += @("-filepath", "frontend/front-end-src.zip")
    }

    & $javaExe -jar VeracodeJavaAPI.jar -vid $env:VERACODE_API_ID -vkey $env:VERACODE_API_SECRET `
        -action UploadAndScan -appname "$veracode_app_name" -createprofile $false -autoscan $true -createsandbox $true -sandboxname "$veracode_sandbox_name" `
        -deleteincompletescan $true @file_upload_args `
        -version "$env:CI_COMMIT_REF_NAME in pipeline $env:CI_PIPELINE_ID" `
        -selectedpreviously $true -scantimeout 60 2>&1 | Tee-Object -FilePath policy_scan_output.txt

    $veracodeExitCode = $LASTEXITCODE
    $scanText = Get-Content ./policy_scan_output.txt -Raw
    $appId = if ($scanText -match 'appid=(\d+)') { $Matches[1] } else { "" }
    $analysisId = if ($scanText -match 'analysis id of the new analysis is "(\d+)"') { $Matches[1] } else { "" }

    if ($appId -and $analysisId) {
        $veracodeScanUrl = "https://analysiscenter.veracode.com/auth/index.jsp#ViewReportsResultSummary:${VeracodeAccountId}:${appId}:${analysisId}"
        Write-Host "Veracode scan results: $veracodeScanUrl"
    } else {
        Write-Host "Could not create veracode policy scan URL."
    }

    if ($veracodeExitCode -ne 0) {
        Write-Error "Veracode Policy Scan failed with exit code $veracodeExitCode"
    }
    exit $veracodeExitCode
}
catch {
    Write-Error "Veracode Policy Scan failed: $_"
    exit 1
}
