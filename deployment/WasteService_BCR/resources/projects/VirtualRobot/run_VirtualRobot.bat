@echo off

:: Docker Desktop default location
set docker_desktop="C:\Program Files\Docker\Docker\Docker Desktop.exe"

:: Counter for attempts
set attempt=1
:: Max num of attempts
set max_attempts=5
:: Delay for attemtps (seconds)
set attempt_delay=5

:: Check if docker is running
docker version > NUL
echo.

if %errorlevel% equ 1 (
	echo Docker daemon is not running.
	echo Starting docker daemon...
	echo.
	
	if exist %docker_desktop% (
		start /b .\run_DockerDesktop.bat
	
	) else (
		echo Docker Desktop not found.
		echo.
		echo Visit https://docs.docker.com/desktop/install/windows-install/ for more info.
		echo.
		
		pause
		exit /b 1
	)
	
) else (
	echo Docker daemon is running
)

:run_image
echo Trying to connect to docker... (Attempt #%attempt%)
docker version > NUL
echo.

if %errorlevel% equ 1 (
	if %max_attempts% gtr 5 (
		exit /b 1
	)
	
	set /a attempt=%attempt%+1
	sleep %attempt_delay%
	
	goto :run_image
)

:: Open Chrome
start explorer http:\\localhost:8090

:: Run Docker Image
docker-compose -f .\virtualRobotOnly4.0.yaml up