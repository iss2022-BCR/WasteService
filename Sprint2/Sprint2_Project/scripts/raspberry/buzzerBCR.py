#!/usr/bin/python3

# Usage: ./displayBCR.py <beep-duration-ms>

import sys
import time

import RPi.GPIO as GPIO

class Buzzer:
        pin = 18
        stateON = False
        
        def __init__(self, pin = 18):
                GPIO.setmode(GPIO.BCM)
                GPIO.cleanup()
                GPIO.setup(pin, GPIO.OUT)
                self.pin = pin
        
        def turnON(self):
                GPIO.output(self.pin, GPIO.HIGH)
                self.stateON = True
        
        def turnOFF(self):
                GPIO.output(self.pin, GPIO.LOW)
                self.stateON = False
        
        def toggle(self):
                self.turnOFF() if self.stateON else self.turnON()

# MAIN =================================
if __name__ == "__main__":
        GPIO.setwarnings(False)
        
        buzzer = Buzzer(18)

        if len(sys.argv) == 1 or len(sys.argv) > 3:
                buzzer.turnOFF()
                exit(1)
                
        if sys.argv[1].lower() == "on":
                buzzer.turnON()
                
        elif (
                sys.argv[1].lower() == "intermit" or
                sys.argv[1].lower() == "intermittent" or
                sys.argv[1].lower() == "intermitting"
        ):
                delay = 100
                try:
                        if 25 <= int(sys.argv[2]) <= 5000:
                                delay = int(sys.argv[2])
                except:
                        pass
                        
                while True:
                        buzzer.toggle()
                        try:
                                time.sleep(delay / 1000)
                        except:
                                buzzer.turnOFF()
                                exit(0)
                        
        else:
                buzzer.turnOFF()






    
