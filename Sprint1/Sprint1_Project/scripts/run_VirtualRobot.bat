:: Run Docker Image
cd ..\..\..\unibo.basicrobot22
start docker-compose -f .\virtualRobotOnly4.0.yaml up

:: Open Chrome
cd "C:\Program Files\Google\Chrome\Application\"
start chrome.exe --new-window http:\\localhost:8090