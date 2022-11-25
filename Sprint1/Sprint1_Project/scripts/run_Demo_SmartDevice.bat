@echo off

set ip=%1
set port=%2
set waste_type=%3
set waste_weight=%4

if "%~1" == "" goto default
if "%~2" == "" goto default
if "%~3" == "" goto default
if "%~4" == "" goto default

echo [SmartDevice Demo] Starting demo with the following parameters:
echo   IP = %ip%
echo   Port = %port%
echo   StoreRequest_Type = %waste_type%
echo   PortStoreRequest_Weight = %waste_weight%

goto end

:default
cd ../sprint1_smart_device/
flutter test ./integration_test/smart_device_demo.dart --dart-define="IP=192.168.1.4" --dart-define="PORT=11800" --dart-define="WASTE_TYPE=GLASS" --dart-define="WASTE_WEIGHT=10.0"

:usage
echo usage: ./demo_sprint1 waste_service_IP waste_service_port waste_type waste_weight
goto end

:end
cd ../sprint1_smart_device/
flutter test ./integration_test/smart_device_demo.dart --dart-define="IP=%ip%" --dart-define="PORT=%port%" --dart-define="WASTE_TYPE=%waste_type%" --dart-define="WASTE_WEIGHT=%waste_weight%"