#!/bin/bash

# Script to build the WasteService_RPi project

cd ../wasteservice_core/

chmod +x gradlew

echo "Building WasteService_Core ZIPs ..."
echo "Building WS ..."
./gradlew distZip -Pbuild_WasteService
echo ""
echo ""

echo "Building TT ..."
./gradlew distZip -Pbuild_TransportTrolley
echo ""
echo ""

echo "Building RT ..."
./gradlew distZip -Pbuild_Robot
echo ""
echo ""

echo "Build complete."