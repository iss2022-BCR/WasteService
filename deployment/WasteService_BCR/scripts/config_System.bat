:: System configuration script
@echo off

setlocal enableextensions enabledelayedexpansion

:: Variables ==============================================
set default_ip=localhost
:: WasteService context IP
set WS_ip=%default_ip%
:: TransportTrolley context IP
set TT_ip=%default_ip%
:: Robot context IP
set RT_ip=%default_ip%
:: Raspberry Pi context IP
set RP_ip=%default_ip%

:: IP of the current host
set ip_address_string="IPv4"
rem Uncomment the following line when using older versions of Windows without IPv6 support (by removing "rem")
rem set ip_address_string="IP Address"
for /f "usebackq tokens=2 delims=:" %%f in (`ipconfig ^| findstr /c:%ip_address_string% ^| findstr /r "192\.168\.1\."`) do (
    set host_ip=%%f

)

:: Remove leading space
set host_ip=%host_ip:~1%

:: ========================================================
cls
echo ========== WasteService Configuration ==========
echo.

echo Current host IP: %host_ip%
echo.

:: Nodes Configuration ====================================
:: Ask for contexts IPs
set /p WS_ip="Enter IP for WasteService context (default: "%default_ip%"): "
call :validateIP %WS_ip% ret
call :validateIP %WS_ip% || set WS_ip=%default_ip%
echo.

set /p TT_ip="Enter IP for TransportTrolley context (default: "%default_ip%"): "
call :validateIP %TT_ip% ret
call :validateIP %TT_ip% || set TT_ip=%default_ip%
echo.

set /p RT_ip="Enter IP for Robot context (default: "%default_ip%"): "
call :validateIP %RT_ip% ret
call :validateIP %RT_ip% || set RT_ip=%default_ip%
echo.

set /p RP_ip="Enter IP for RaspberryPi context (default: "%default_ip%"): "
call :validateIP %RP_ip% ret
call :validateIP %RP_ip% || set RT_ip=%default_ip%
echo.

echo ------------------- Summary --------------------
echo.

echo IP host:   %host_ip% (localhost)
echo.
echo IP Ctx_WS: %WS_ip%
echo IP Ctx_TT: %TT_ip%
echo IP Ctx_RT: %RT_ip%
echo IP Ctx_RP: %RP_ip%
echo.

pause

exit 0
  
:: Functions ==============================================
:validateIP ipAddress [returnVariable]
    rem prepare environment
    setlocal 

    rem asume failure in tests : 0=pass 1=fail : same for errorlevel
    set "_return=1"

    rem test if address conforms to ip address structure
    echo %~1^| findstr /b /e /r "[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*" >nul

    rem if it conforms to structure, test each octet for rage values
    if not errorlevel 1 for /f "tokens=1-4 delims=." %%a in ("%~1") do (
        if %%a gtr 0 if %%a lss 255 if %%b leq 255 if %%c leq 255 if %%d gtr 0 if %%d leq 254 set "_return=0"
    )

:endValidateIP
    rem clean and return data/errorlevel to caller
    endlocal & ( if not "%~2"=="" set "%~2=%_return%" ) & exit /b %_return%
	
:: TEST ===================================================
setlocal enableextensions enabledelayedexpansion

    rem try some ip addresses 
    for %%i in ("localhost" "raspberrypi.local" "1.1.1.1" "0.1.1.1" "250.1024.1.1" "10.0.2.1" "something" "" ) do (

        echo --------------------------------------------

        rem call with a variable to get return value
        call :validateIP %%~i ret 
        echo %%~i : return value : !ret! 
		
        rem call with or without variable to get errorlevel
        call :validateIP %%~i  && echo %%i is valid || echo %%i is invalid

    ) 