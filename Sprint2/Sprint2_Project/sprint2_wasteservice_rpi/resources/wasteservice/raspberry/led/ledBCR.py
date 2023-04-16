#!/usr/bin/python3

# Usage: ./ledBCR.py <on | off | blink [delay]>

import sys
import time

import RPi.GPIO as GPIO

class Led:
        pin = 25
        stateON = False
        
        def __init__(self, pin = 25):
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

        led = Led(25)

        if len(sys.argv) == 1 or len(sys.argv) > 3:
                led.turnOFF()
                exit(1)

        if sys.argv[1].lower() == "on":
                led.turnON()
                
        elif (
                sys.argv[1].lower() == "blink" or
                sys.argv[1].lower() == "blinking"
        ):
                delay = 100
                try:
                        if 25 <= int(sys.argv[2]) <= 5000:
                                delay = int(sys.argv[2])
                except:
                        pass
                        
                while True:
                        led.toggle()
                        try:
                                time.sleep(delay / 1000)
                        except:
                                led.turnOFF()
                                exit(0)
                        
        else:
                led.turnOFF()





