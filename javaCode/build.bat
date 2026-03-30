@echo off
echo Building Campus Course & Records Manager (CCRM)...

REM Create output directory
if not exist "build" mkdir build

REM Compile all Java files
echo Compiling Java source files...
javac -d build -cp "src/main/resources" src/main/java/edu/campus/ccrm/*.java src/main/java/edu/campus/ccrm/**/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo.
    echo To run the application:
    echo java -cp build edu.campus.ccrm.CampusRecordsManager
    echo.
) else (
    echo Compilation failed!
    exit /b 1
)

pause
