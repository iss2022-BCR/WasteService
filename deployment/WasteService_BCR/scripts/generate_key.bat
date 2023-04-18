@echo off

set root=..\
set dir_out=resources\ssh\
set key_name=key_rpi

cd %root%%dir_out%

:: Check if already exist
if exist %key_name%* (
    echo A key named "%key_name%" already exists.
	set /p confirm_overwrite=Overwrite ?
	echo %confirm_overwrite%
	if /i "%confirm_overwrite%" neq "Y" (
		echo Key not overwritten.
		exit
	)
)

:: Generate key
ssh-keygen.exe -q -f %key_name% -t ed25519 -N '""'

echo.
echo Key generated in %root%%dir_out%
echo.

pause