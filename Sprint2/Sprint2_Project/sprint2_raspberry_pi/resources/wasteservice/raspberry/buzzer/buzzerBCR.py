#!/usr/bin/python3

# Usage: ./displayBCR.py <beep-duration-ms>

import sys
import time

import RPi.GPIO as GPIO
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.cleanup()

GPIO_BUZZER = 18

GPIO.setup(GPIO_BUZZER, GPIO.OUT)

if len(sys.argv) > 2:
        GPIO.output(GPIO_BUZZER, GPIO.LOW)
        exit(1)
        
duration = 25

if len(sys.argv) == 2:
        try:
                duration = int(sys.argv[1])
        except:
                pass

try:
        GPIO.output(GPIO_BUZZER, GPIO.HIGH)
        time.sleep(duration / 1000)
        GPIO.output(GPIO_BUZZER, GPIO.LOW)
except:
        GPIO.output(GPIO_BUZZER, GPIO.LOW)