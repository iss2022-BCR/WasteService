@echo off

cd ../wasteservice_core/

:: Run Robot Context
start .\gradlew runCtx_Robot

:: Run TransportTrolley Context
start .\gradlew runCtx_TransportTrolley

:: Run WasteService Context
start .\gradlew runCtx_WasteService