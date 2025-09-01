@echo off
echo Inventory Management System - Supabase Edition
echo =============================================

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

REM Check Java version
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%g
    goto :check_version
)
:check_version
echo Found Java version: %JAVA_VERSION%
echo %JAVA_VERSION% | findstr /r "1[7-9]\|2[0-9]" >nul
if %errorlevel% neq 0 (
    echo Error: Java 17 or higher is required
    echo Current version: %JAVA_VERSION%
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo Building project...
call mvn clean compile

if %errorlevel% neq 0 (
    echo Error: Build failed
    pause
    exit /b 1
)

echo Starting application with JavaFX...
call mvn javafx:run

pause 