#!/bin/bash

if [[ $1 == "-m" ]]; then  # If the first argument is "-m" (macOS)
  open -a Safari http://localhost:8090  # Open Safari
elif [[ $1 == "-l" ]]; then  # If the first argument is "-l" (Linux)
   xdg-open http://localhost:8090 &  # Open the default browser in the background
else
  echo "Error: Invalid argument. Usage: ./script.sh [-m | -l]"  # Print an error message if the argument is invalid
  exit 1  # Exit with an error code
fi

# Run Docker Image
cd ../../../unibo.basicrobot22
docker-compose -f ./virtualRobotOnly4.0.yaml up
