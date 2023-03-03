/**
* Sonar Alone by team BCR
*
* Build with: gcc SonarAloneBCR.c -o SonarAloneBCR -lwiringPi
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include <signal.h>
#include <unistd.h>
#include <wiringPi.h>

#define GPIO_BUZZER 1	// 18
#define GPIO_TRIGGER 4	// 23
#define GPIO_ECHO 5	// 24

#define DEFAULT_BUZZER 0
#define DEFAULT_DELAY 500
#define DEFAULT_RANDOM 0
#define DEFAULT_UNIT 2
#define DEFAULT_LOWER_BOUND 4
#define DEFAULT_UPPER_BOUND 300

enum Flags
{
	FLAG_BUZZER,	        // -b/--buzzer
	FLAG_DELAY,	        // -d/--delay <delay-amount>
	FLAG_RANDOM,            // -r/--random
	FLAG_UNIT,	        // --unit-of-measure <cm | mm>
	FLAG_LOWER_BOUND,       // --lower-bound <lower-bound>
	FLAG_UPPER_BOUND,       // --upper-bound <upper-bound>
	FLAG_ERR,
	FLAG_MAX,
};

enum Unit
{
        CM =    1,
        MM =    10,
};

// Function declarations
void printUsage(char* cmd);
void parseArguments(int argc, char** argv);
int init();
float getDistance(int unit);
float getRandomDistance(int unit, int lowerBound, int upperBound);
void doBuzz(int delay);

void cleanup(int);
void setOnQuit(void (*function)(int));

// Global variables
int argFlags[FLAG_MAX];

// Main
int main(int argc, char** argv)
{
        int distance;

        init();
        parseArguments(argc, argv);
        setOnQuit(cleanup);
        
        for(;;)
        {
                if(argFlags[FLAG_RANDOM])
                {
                        distance = getRandomDistance(argFlags[FLAG_UNIT], argFlags[FLAG_LOWER_BOUND], argFlags[FLAG_UPPER_BOUND]);
                }
                else distance = (int) getDistance(argFlags[FLAG_UNIT]);
                
                // Print the distance if it's between the bounds
                if(distance >= (argFlags[FLAG_LOWER_BOUND] * argFlags[FLAG_UNIT]) &&
                        distance <= (argFlags[FLAG_UPPER_BOUND] * argFlags[FLAG_UNIT]))
                        printf("%d\n", distance);
                
                // Make a beep sound
                if(argFlags[FLAG_BUZZER])
                        doBuzz(25);
                
                
                delay(argFlags[FLAG_DELAY]);
        }
        
        return 0;
}

// Function definitions
void printUsage(char* cmd)
{
	printf("USAGE:\n\t%s [-b/--buzzer] [-d/--delay <delay-amount>] [--unit-of-measure <m | dm | cm | mm>] [--lower-bound <lower-bound>] [--upper-bound <upper-bound>] [-r/--random]\n\n\n", cmd);
	//printf("-i/--ip <ip_address>, speficies the IP address of the remote host on which send the packets (default is %s)\n\n-p/--port <port>, specifies the remote port on which send the packets (default is %d)\n\n-r/--random, specifies that the distance will be generated with a RNG\n\n-D/--distance <distance>, specifies the distance lower limit under which the led will light up (default is %3.1f)\n\n", argv[0], DEFAULT_IP, DEFAULT_SND_PORT, DEFAULT_DLIM);
}

void parseArguments(int argc, char** argv)
{
        memset(argFlags, 0, sizeof(argFlags));
        
        int lowerBound = DEFAULT_LOWER_BOUND;
        int upperBound = DEFAULT_UPPER_BOUND;
        
        for (int i = 1; i < argc; i++)
	{
	        if (strcmp(argv[i], "-b") == 0 || strcmp(argv[i], "--buzzer") == 0)
		{
	                if (argFlags[FLAG_BUZZER])
			{
				argFlags[FLAG_ERR] = 1;
				break;
			}
			argFlags[FLAG_BUZZER] = 1;
		}
		else if (strcmp(argv[i], "-d") == 0 || strcmp(argv[i], "--delay") == 0)
		{
			if (argFlags[FLAG_DELAY])
			{
				argFlags[FLAG_ERR] = 1;
				break;
			}
			i++;
			int delay = atoi(argv[i]);
			if (delay < 50 || delay > 2000)
			{
			        delay = DEFAULT_DELAY;
			}
			argFlags[FLAG_DELAY] = delay;  
		}
		else if (strcmp(argv[i], "-r") == 0 || strcmp(argv[i], "--random") == 0)
		{
		        if (argFlags[FLAG_RANDOM])
			{
				argFlags[FLAG_ERR] = 1;
				break;
			}
			argFlags[FLAG_RANDOM] = 1;
		}
		else if (strcmp(argv[i], "-u") == 0 || strcmp(argv[i], "--unit") == 0)
		{
			if (argFlags[FLAG_LOWER_BOUND])
			{
				argFlags[FLAG_ERR] = 1;
				break;
			}
			i++;
		        if(strcmp(argv[i], "cm") == 0 || strcmp(argv[i], "CM") == 0)
		                argFlags[FLAG_UNIT] = CM;
		        else if(strcmp(argv[i], "mm") == 0 || strcmp(argv[i], "MM") == 0)
		                argFlags[FLAG_UNIT] = MM;
		        else argFlags[FLAG_UNIT] = DEFAULT_UNIT;
                        
		}
		else if (strcmp(argv[i], "--lower-bound") == 0)
		{
			if (argFlags[FLAG_LOWER_BOUND])
			{
				argFlags[FLAG_ERR] = 1;
				break;
			}
			i++;
			lowerBound = atoi(argv[i]);
			if (lowerBound < DEFAULT_LOWER_BOUND || lowerBound > DEFAULT_UPPER_BOUND ||
			        lowerBound > upperBound)
			{
			        lowerBound = DEFAULT_LOWER_BOUND;
			}
			argFlags[FLAG_LOWER_BOUND] = lowerBound;  
		}
		else if (strcmp(argv[i], "--upper-bound") == 0)
		{
			if (argFlags[FLAG_UPPER_BOUND])
			{
				argFlags[FLAG_ERR] = 1;
				break;
			}
			i++;
			upperBound = atoi(argv[i]);
			if (upperBound < DEFAULT_LOWER_BOUND || upperBound > DEFAULT_UPPER_BOUND ||
			        upperBound < lowerBound)
			{
			        upperBound = DEFAULT_UPPER_BOUND;
			}
			argFlags[FLAG_UPPER_BOUND] = upperBound;  
		}
	}
	
	if (argFlags[FLAG_ERR])
	{
		printUsage(argv[0]);
		exit(1);
	}
	if (!argFlags[FLAG_DELAY])
		argFlags[FLAG_DELAY] = DEFAULT_DELAY;
	if (!argFlags[FLAG_UNIT])
		argFlags[FLAG_UNIT] = DEFAULT_UNIT;
	if (!argFlags[FLAG_LOWER_BOUND])
		argFlags[FLAG_LOWER_BOUND] = DEFAULT_LOWER_BOUND;
	if (!argFlags[FLAG_UPPER_BOUND])
		argFlags[FLAG_UPPER_BOUND] = DEFAULT_UPPER_BOUND;
}


/**
* Init Sonar Alone. Return >= 0 if everything set up correctly, < 0 otherwise.
*/
int init()
{
        // Init random seed
	srand(time(NULL));
	
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
float getDistance(int unit)
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
        
        timeElapsed *= unit;
        
        // multiply with the sonic speed (34300 cm/s) and divide by 2
        return (timeElapsed * 34300) / 2;
}
float getRandomDistance(int unit, int lowerBound, int upperBound)
{
        int distance = lowerBound + rand() / (RAND_MAX / (upperBound - lowerBound + 1) + 1);
        
        distance *= unit;
        
        return distance;
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