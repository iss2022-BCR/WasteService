@echo off

:: Docker Desktop default location
set docker_desktop="C:\Program Files\Docker\Docker\Docker Desktop.exe"

:: Check if docker is running
docker version
echo.

start /b %docker_desktop%

pause

if %errorlevel% equ 1 (
	echo Docker daemon is not running.
	echo Starting docker daemon...
	echo.
	
	echo %docker_desktop%
	if exist %docker_desktop% (
		
		timeout /t 5
		
	) else (
		echo Docker Desktop not found.
		echo.
		echo Visit https://docs.docker.com/desktop/install/windows-install/ for more info.
		echo.
		
		exit /b 1
	)
	
) else (
	echo Docker daemon is running
)

:run_image
:: Run Docker Image
docker-compose -f .\virtualRobotOnly4.0.yaml up

:: Open Chrome
start explorer http:\\localhost:8090

pause