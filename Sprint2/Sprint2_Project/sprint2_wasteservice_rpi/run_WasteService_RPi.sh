#!/bin/bash

function clearDevices() {
        echo "Clearing devices..."
        for file in *.py
        do
                python3 "$file" > /dev/null
        done
}



cd bin/

clearDevices

# Start
echo "Starting WasteService RPi..."
./unibo.sprint2_wasteservice_rpi

clearDevices

