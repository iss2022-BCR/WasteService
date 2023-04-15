#!/usr/bin/python3

# Usage: ./sonarBCR.py <loop> [delay]

import sys
import time

import RPi.GPIO as GPIO

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


# MAIN =================================
if __name__ == "__main__":
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)

        GPIO_TRIGGER = 23
        GPIO_ECHO = 24

        GPIO.setup(GPIO_TRIGGER, GPIO.OUT)
        GPIO.setup(GPIO_ECHO, GPIO.IN)
        
        loop = False
        delayTime = 250
        
        # Parameters check
        if len(sys.argv) > 3:
                exit(1)
        
        if (
                len(sys.argv) > 1 and
                (
                        sys.argv[1].lower() == "loop" or
                        sys.argv[1].lower() == "looped" or
                        sys.argv[1].lower() == "looping"
                )
        ):
                loop = True        
                if len(sys.argv) == 3:
                        if 50 <= int(sys.argv[2]) <= 5000:
                                delayTime = int(sys.argv[2])
        
        try:
                
                distance = distanceCM()
                print(int(distance))
                
                while(loop):
                        time.sleep(delayTime / 1000)
                        distance = distanceCM()
                        print(int(distance))
                        
        except:
                pass
        #print("Measured distance = %.1f cm" % distance)
        GPIO.cleanup()




