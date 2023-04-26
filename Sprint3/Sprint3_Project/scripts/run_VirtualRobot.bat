:: Run Docker Image
cd ..\..\..\unibo.basicrobot22
start docker-compose -f .\virtualRobotOnly4.0.yaml up

:: Open Chrome
start explorer http:\\localhost:8090