#!/bin/bash

# Script to deploy the WasteService_RPi project to the Raspberry Pi

# Variables ==============================================
# Project name
project_name=unibo.sprint2_wasteservice_rpi-1.0

# ZIP filename
zip_name=${project_name}.zip

# IP address of remote host
ip_address=192.168.1.5

# Remote deploy location
path=/home/pi/wasteservice/

# Deployment =============================================
# Build the ZIP
./build_WasteService_RPi.sh
echo
echo

# Ask IP address of the target host
read -p "Enter Raspberry Pi IP address: " ip_address
echo
echo

# Create empty directory
echo "Create deployment directory ..."
ssh pi@${ip_address} "rm -fdr ${path} && mkdir ${path}"
echo
echo

# Copy the ZIP on remote host
echo "Copying ZIP on ${ip_address}:/home/pi/wasteservice ..."
scp ../sprint2_wasteservice_rpi/build/distributions/${zip_name} pi@${ip_address}:${path}
echo
echo

# Setup (cd to location, force remove old project, unzip)
echo "Remote Setup ..."
ssh pi@${ip_address} "cd ${path} && rm -fdr ${project_name} && unzip ${zip_name} && chmod +x ${project_name}/run_WasteService_RPi.sh"
echo
echo

echo "Deployment completed."
