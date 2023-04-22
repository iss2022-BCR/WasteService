@echo off

:: Docker Desktop default location
set docker_desktop="C:\Program Files\Docker\Docker\Docker Desktop.exe"

if exist %docker_desktop% (

	%docker_desktop%
	
) else (
	echo Docker Desktop not found.
	echo.
	echo Visit https://docs.docker.com/desktop/install/windows-install/ for more info.
	echo.
	
	exit /b 1
)