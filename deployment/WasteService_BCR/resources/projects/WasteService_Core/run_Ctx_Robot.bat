cd bin\

setlocal
call :setESC

for /f "delims=" %%a in ('dir /b /a-d <PATH>') do (echo %ESC%%%a%ESC%[0m)
.\unibo.sprint2_wasteservice_core_RT.bat

:setESC
for /F "tokens=1,2 delims=#" %%a in ('"prompt #$H#$E# & echo on & for %%b in (1) do rem"') do (
  set ESC=%%b
  exit /B 0
)
exit /B 0