:: Script to build the WasteService_RPi project
@echo off

cd ..\sprint2_wasteservice\

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