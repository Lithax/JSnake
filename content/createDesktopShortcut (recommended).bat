@echo off
setlocal

:: Define paths
set "JAR_PATH=%~dp0snake.jar"
set "ICON_PATH=%~dp0snakethumbnail1.ico"

:: Check for OneDrive Desktop path
set "DESKTOP_PATH=%USERPROFILE%\Desktop"

:: Check for OneDrive Desktop folder in Documents and Dokumente
if exist "%USERPROFILE%\OneDrive\Documents\Desktop\" (
    set "DESKTOP_PATH=%USERPROFILE%\OneDrive\Documents\Desktop"
) else if exist "%USERPROFILE%\OneDrive\Dokumente\Desktop\" (
    set "DESKTOP_PATH=%USERPROFILE%\OneDrive\Dokumente\Desktop"
)

set "SHORTCUT_PATH=%DESKTOP_PATH%\Snake.lnk"

:: Display paths for debugging
echo.
echo JAR_PATH: %JAR_PATH%
echo ICON_PATH: %ICON_PATH%
echo SHORTCUT_PATH: %SHORTCUT_PATH%
echo.

:: Create the Desktop directory if it does not exist
if not exist "%DESKTOP_PATH%" (
    echo Desktop directory does not exist. Exiting.
    exit /b 1
)

:: Create a basic shortcut
echo Creating basic shortcut...
powershell -Command ^
    "$shortcut = (New-Object -COM WScript.Shell).CreateShortcut('%SHORTCUT_PATH%');" ^
    "$shortcut.TargetPath = '%JAR_PATH%';" ^
    "$shortcut.Save()"

:: Set the icon for the shortcut
echo Setting icon for the shortcut...
powershell -Command ^
    "$shortcut = (New-Object -COM WScript.Shell).CreateShortcut('%SHORTCUT_PATH%');" ^
    "$shortcut.IconLocation = '%ICON_PATH%';" ^
    "$shortcut.Save()"

:: Confirm completion
echo Shortcut creation and icon setting completed.
