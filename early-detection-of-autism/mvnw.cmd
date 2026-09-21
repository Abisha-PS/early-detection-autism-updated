@REM ----------------------------------------------------------------------------
@REM Maven Wrapper Batch Script for Windows
@REM ----------------------------------------------------------------------------
@echo off
setlocal

set "DIR=%~dp0"
set "MAVEN_WRAPPER_DIR=%DIR%.mvn\wrapper"
set "MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6\bin"

if exist "%MAVEN_HOME%\mvn.cmd" (
    "%MAVEN_HOME%\mvn.cmd" %*
    exit /b %ERRORLEVEL%
)

echo [INFO] Apache Maven not detected in wrapper directory.
echo [INFO] Downloading and bootstrapping Apache Maven 3.9.6 automatically...

powershell -NoProfile -ExecutionPolicy Bypass -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $zip = Join-Path $env:TEMP 'maven.zip'; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip' -OutFile $zip; Expand-Archive -Path $zip -DestinationPath (Join-Path $env:USERPROFILE '.m2\wrapper\dists') -Force; Remove-Item $zip }"

if exist "%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6\bin\mvn.cmd" (
    echo [INFO] Maven bootstrap complete. Running build...
    "%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6\bin\mvn.cmd" %*
    exit /b %ERRORLEVEL%
) else (
    echo [ERROR] Could not automatically bootstrap Maven. Please install Maven or run from your IDE.
    exit /b 1
)

