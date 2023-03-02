/**
* Sonar Alone by team BCR
* 
* Build with: gcc SonarAloneBCR.c -o SonarAloneBCR -lwiringPi
*/

#include <stdio.h>
#include <stdlib.h>
#include <wiringPi.h>

#define GPIO_BUZZER 1	// 18
#define GPIO_TRIGGER 4	// 23
#define GPIO_ECHO 5	// 24

void printUsage(int argc, char** argv);
void parseParameters();
int init();
float distanceCM();

int main(int argc, char** argv)
{
        init();
        // parse arguments
        
        for(;;)
        {
                printf("%d\n", (int) distanceCM());
                delay(500);
        }
        
        return 0;
}


/**
* Init Sonar Alone. Return >= 0 if everything set up correctly, < 0 otherwise.
*/
int init()
{
        #if(defined __unix__ && __arm__)
                wiringPiSetup();
	        pinMode(GPIO_BUZZER, OUTPUT);
	        pinMode(GPIO_TRIGGER, OUTPUT);
	        pinMode(GPIO_ECHO, INPUT);
        #else
                return -1;
        #endif
        
        return 0;
}

/**
* Get the distance from the sonar in CM.
*/
float distanceCM()
{
        float startTime, stopTime, timeElapsed;
        
        // Set TRIGGER to HIGH
        digitalWrite(GPIO_TRIGGER, HIGH);
        
        // Set TRIGGER to LOW (after 1 millisecond)
        delayMicroseconds(100);
        digitalWrite(GPIO_TRIGGER, LOW);
        
        // Wait until ECHO is LOW: save start time
        while(digitalRead(GPIO_ECHO) == LOW)
                startTime = micros();
        
        // Wait until ECHO is HIGH: save stop time
        while(digitalRead(GPIO_ECHO) == HIGH)
                stopTime = micros();
        
        // time difference between start and arrival
        timeElapsed = (stopTime - startTime) / 1000000;
        
        // multiply with the sonic speed (34300 cm/s) and divide by 2
        return (timeElapsed * 34300) / 2;
}