:: Script to build the WasteService_RPi project
@echo off

cd ..\..\wasteservice_rpi\

echo Building WasteService_RPi ZIP ...
.\gradlew distZip

pause