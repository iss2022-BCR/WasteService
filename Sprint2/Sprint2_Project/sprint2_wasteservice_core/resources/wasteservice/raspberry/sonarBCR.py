#!/usr/bin/python3

import time
import RPi.GPIO as GPIO
GPIO.setwarnings(False)

GPIO.setmode(GPIO.BCM)

GPIO_TRIGGER = 23
GPIO_ECHO = 24

GPIO.setup(GPIO_TRIGGER, GPIO.OUT)
GPIO.setup(GPIO_ECHO, GPIO.IN)

def distanceCM():
        # Set TRIGGER to HIGH
        GPIO.output(GPIO_TRIGGER, GPIO.HIGH)

        #Set TRIGGER to LOW (after 0.01 milliseconds)
        time.sleep(0.00001)
        GPIO.output(GPIO_TRIGGER, GPIO.LOW)
        
        startTime = None
        stopTime = None
        
        # Wait until ECHO is LOW: save start time
        while GPIO.input(GPIO_ECHO) == GPIO.LOW:
                startTime = time.time()

        # Wait until ECHO is HIGH: save stop time
        while GPIO.input(GPIO_ECHO) == GPIO.HIGH:
                stopTime = time.time()
                
        # Time difference between start and stop
        timeElapsed = stopTime - startTime;
        
        # Multiply with the sonic speed (34300 cm/s) and divide by 2
        distance = (timeElapsed * 34300) / 2
        
        return distance
 

try:
        distance = distanceCM()
        print(int(distance))
except:
        pass
#print("Measured distance = %.1f cm" % distance)
GPIO.cleanup()