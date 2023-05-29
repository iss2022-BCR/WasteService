@echo off

cd ..\..\wasteservice_gui\

:: Run GUI
start .\gradlew bootRun

:: Open Chrome
start explorer http:\\localhost:11804