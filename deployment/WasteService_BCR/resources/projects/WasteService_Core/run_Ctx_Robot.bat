cd bin\

for /f "delims=" %%a in ('.\unibo.sprint2_wasteservice_core_RT.bat') do (echo %%a)

exit

setlocal
call :setESC


.\unibo.sprint2_wasteservice_core_RT.bat

:setESC
for /F "tokens=1,2 delims=#" %%a in ('"prompt #$H#$E# & echo on & for %%b in (1) do rem"') do (
  set ESC=%%b
  exit /B 0
)
exit /B 0