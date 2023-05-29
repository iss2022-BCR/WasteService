:: Script to deploy the WasteService_RPi project to the Raspberry Pi
@echo off

:: Variables ==============================================
:: Project name
set project_name=unibo.wasteservice_rpi-1.0

:: ZIP filename
set zip_name=%project_name%.zip

:: IP address of remote host
set ip_address=192.168.1.5
set ssh_port=11000

:: Remote deploy location
set path=/home/pi/wasteservice/

:: Deployment =============================================
set /p confirm_build=Build the ZIP (Y/[N])?
if /i "%confirm_build%" equ "Y" (
	:: Build the ZIP
	call build_WasteService_RPi.bat
	echo.
	echo.
)


:: Ask IP address of the target host
:: set /p ip_address="Enter Raspberry Pi IP address: "
:: echo.
:: echo.

:: Create empty directory
echo Create deployment directory ...
"%windir%\System32\OpenSSH\ssh.exe" pi@%ip_address% -p %ssh_port% "rm -fdr %path% && mkdir %path%"
echo.
echo.

:: Copy the ZIP on remote host
echo Copying ZIP on %ip_address%:/home/pi/wasteservice ...
"%windir%\System32\OpenSSH\scp.exe" -P %ssh_port% ..\..\wasteservice_rpi\build\distributions\%zip_name% pi@%ip_address%:%path%
echo.
echo.

:: Setup (cd to location, force remove old project, unzip)
echo Remote Setup ...
"%windir%\System32\OpenSSH\ssh.exe" pi@%ip_address% -p %ssh_port% "cd %path% && rm -fdr %project_name% && unzip %zip_name% && chmod +x %project_name%/run_WasteService_RPi.sh && chmod +x %project_name%/bin/unibo.sprint2_wasteservice_rpi"
echo.
echo.

echo Deployment completed.

pause