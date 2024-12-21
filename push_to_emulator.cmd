@echo off
setlocal enabledelayedexpansion

REM Set Android SDK paths
set "ANDROID_HOME=C:\Users\Ali\AppData\Local\Android\Sdk"
set "ADB=%ANDROID_HOME%\platform-tools\adb.exe"
set "EMULATOR=%ANDROID_HOME%\emulator\emulator.exe"

REM Add platform-tools to PATH temporarily
set "PATH=%ANDROID_HOME%\platform-tools;%PATH%"

REM Check if ADB exists
if not exist "%ADB%" (
    echo Error: ADB not found at %ADB%
    echo Please ensure Android SDK is installed correctly
    exit /b 1
)

REM Check if Emulator exists
if not exist "%EMULATOR%" (
    echo Error: Emulator not found at %EMULATOR%
    echo Please ensure Android SDK is installed correctly
    exit /b 1
)

if "%1"=="" goto usage
if "%1"=="start" goto start
if "%1"=="push" goto push
goto invalid

:usage
echo Usage:
echo   push_to_emulator.cmd push [device-id]   - Push to emulator (optional device-id for multiple devices)
echo   push_to_emulator.cmd start              - Start the emulator
exit /b 1

:start
echo Checking for running emulator...
for /f "tokens=*" %%a in ('%ADB% devices') do (
    set line=%%a
    if "!line:emulator=!" neq "!line!" (
        set emulator_found=true
    )
)

if not defined emulator_found (
    echo No emulator found running. Starting default emulator...
    start /b %EMULATOR% -avd Pixel_3a_API_34_extension_level_7_x86_64
    echo Waiting for emulator to start...
    %ADB% wait-for-device
) else (
    echo Emulator is already running
)
exit /b 0

:push
REM List available devices
echo Available devices:
adb devices
echo.

REM Check if a specific device was provided
if not "%2"=="" (
    REM Verify the specified device exists
    for /f "tokens=1" %%a in ('adb devices ^| findstr "%2"') do (
        if "%%a"=="%2" (
            echo Using specified device: %2
            set "DEVICE_ID=%2"
            goto push_apk
        )
    )
    echo Error: Device %2 not found
    exit /b 1
) else (
    REM Get the first available device
    for /f "skip=1 tokens=1" %%a in ('adb devices ^| findstr "device$"') do (
        set "DEVICE_ID=%%a"
        goto push_apk
    )
    
    if not defined DEVICE_ID (
        echo No devices/emulators found. Please start an emulator first.
        exit /b 1
    )
)

:push_apk
echo Using device: %DEVICE_ID%

echo Installing APK on device...
adb -s %DEVICE_ID% install -r app\build\outputs\apk\debug\app-debug.apk
if errorlevel 1 (
    echo Installation failed
    exit /b 1
)

echo Launching app...
adb -s %DEVICE_ID% shell am start -n com.example.myownchat/.MainActivity
echo Done!
exit /b 0

:invalid
echo Invalid argument: %1
echo Usage:
echo   push_to_emulator.cmd push [device-id]   - Push to emulator (optional device-id for multiple devices)
echo   push_to_emulator.cmd start              - Start the emulator
exit /b 1
