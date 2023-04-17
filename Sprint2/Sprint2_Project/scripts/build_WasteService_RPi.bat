:: Script to build the WasteService_RPi project
@echo off

cd ..\sprint2_wasteservice_rpi\

echo Building WasteService_RPi ZIP ...
.\gradlew distZip

pause