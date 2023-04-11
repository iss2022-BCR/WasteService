@echo off
:loop

set /a num=%random% %%100

echo %num%

sleep 1

goto loop