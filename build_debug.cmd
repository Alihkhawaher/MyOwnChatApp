@echo off
echo Building debug APK...
call gradlew.bat assembleDebug
if errorlevel 1 (
    echo Build failed
    exit /b 1
)
echo Build completed successfully
