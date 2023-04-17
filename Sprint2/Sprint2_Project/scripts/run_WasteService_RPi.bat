:: Script to run the WasteService_RPi project on the Raspberry Pi
@echo off

:: Variables ==============================================
:: Project name
set project_name=unibo.sprint2_wasteservice_rpi-1.0

:: ZIP filename
set zip_name=%project_name%.zip

:: IP address of remote host
set ip_address=192.168.1.5

:: Remote deploy location
set path=/home/pi/wasteservice

:: Deployment =============================================
:: Ask IP address of the target host
set /p ip_address="Enter Raspberry Pi IP address: "
echo.
echo.

:: Connect to the remote Raspberry Pi
"%windir%\System32\OpenSSH\ssh.exe" pi@%ip_address% "cd %path%/%project_name% && ./run_WasteService_RPi.sh"