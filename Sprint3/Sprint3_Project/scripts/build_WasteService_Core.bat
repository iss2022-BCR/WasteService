:: Script to build the WasteService_Core project
@echo off

cd ..\wasteservice_core\

echo Building WasteService_Core ZIPs ...

echo Building WS ...
call .\gradlew distZip -Pbuild_WasteService
echo.
echo.

echo Building TT ...
call .\gradlew distZip -Pbuild_TransportTrolley
echo.
echo.

echo Building RT ...
call .\gradlew distZip -Pbuild_Robot
echo.
echo.

echo build complete.

pause