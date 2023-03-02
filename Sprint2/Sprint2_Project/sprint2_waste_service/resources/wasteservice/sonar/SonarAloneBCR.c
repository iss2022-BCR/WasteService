/**
* Sonar Alone by team BCR
*
* Build with: gcc SonarAloneBCR.c -o SonarAloneBCR -lwiringPi
*/

#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <wiringPi.h>

#define GPIO_BUZZER 1	// 18
#define GPIO_TRIGGER 4	// 23
#define GPIO_ECHO 5	// 24

enum Flags
{
	FLAG_IP,	// -i/--ip <ip_address>
	FLAG_PORT,	// -p/--port <port> || -d/--direction <direction>
	FLAG_RAND,	// -r/--random
	FLAG_DIST,	// -d/--distance <distance>
	FLAG_ERR,
	FLAG_MAX,
};

void printUsage();
void parseParameters(int argc, char** argv);
int init();
float distanceCM();
void doBuzz(int delay);

void cleanup(int);
void setOnQuit(void (*function)(int));

int main(int argc, char** argv)
{
        init();
        // parse arguments
        setOnQuit(cleanup);
        
        for(;;)
        {
                printf("%d\n", (int) distanceCM());
                
                doBuzz(25);
                
                
                delay(500);
        }
        
        return 0;
}

void printUsage(char* cmd)
{
	printf("USAGE:\n\t%s [-b/--buzzer] [-d/--delay <delay-value>] [--unit-of-measure <m | dm | cm | mm>] [--lower-bound <lower-bound>] [--upper-bound <upper-bound>] [-r/--random]\n\n\n", cmd);
	//printf("-i/--ip <ip_address>, speficies the IP address of the remote host on which send the packets (default is %s)\n\n-p/--port <port>, specifies the remote port on which send the packets (default is %d)\n\n-r/--random, specifies that the distance will be generated with a RNG\n\n-D/--distance <distance>, specifies the distance lower limit under which the led will light up (default is %3.1f)\n\n", argv[0], DEFAULT_IP, DEFAULT_SND_PORT, DEFAULT_DLIM);
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
void doBuzz(int delayAmount)
{
        digitalWrite(GPIO_BUZZER, HIGH);
        delay(delayAmount);
        digitalWrite(GPIO_BUZZER, LOW);
}

void setOnQuit(void (*function)(int))
{
        // set handler for cleanup
	struct sigaction sigIntHandler;

	sigIntHandler.sa_handler = function;
	sigemptyset(&sigIntHandler.sa_mask);
	sigIntHandler.sa_flags = 0;

	sigaction(SIGINT, &sigIntHandler, NULL);
}

void cleanup(int value)
{
	digitalWrite(GPIO_BUZZER, LOW);
	digitalWrite(GPIO_TRIGGER, LOW);
	exit(0);
}