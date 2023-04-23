/**
* Sonar Wrapper by team BCR
*
* Build with: gcc SonarWrapperBCR.c -o SonarWrapperBCR
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include <signal.h>
#include <unistd.h>

#define DEFAULT_BUZZER 0
#define DEFAULT_DELAY 500
#define DEFAULT_RANDOM 0
#define DEFAULT_LOWER_BOUND 4
#define DEFAULT_UPPER_BOUND 300

enum Flags
{
	FLAG_BUZZER,	        // -b/--buzzer
	FLAG_DELAY,	        // -d/--delay <delay-amount>
	FLAG_DISPLAY,           // -D/--display
	FLAG_RANDOM,            // -r/--random
	FLAG_LOWER_BOUND,       // -l/--lower-bound <lower-bound>
	FLAG_UPPER_BOUND,       // -u/--upper-bound <upper-bound>
	FLAG_ERR,
	FLAG_MAX,
};

// Function declarations
void printUsage(char* cmd);
void parseArguments(int argc, char** argv);
float getRandomDistance(int lowerBound, int upperBound);

void cleanup(int);
void setOnQuit(void (*function)(int));

// Global variables
int argFlags[FLAG_MAX];

// Main
int main(int argc, char** argv)
{
        FILE* fp;
        char cmd[64], cmdOutput[16];
        int distance;

        parseArguments(argc, argv);
        setOnQuit(cleanup);
        
        for(;;)
        {
                        
                if(argFlags[FLAG_RANDOM])
                {
                        distance = getRandomDistance(argFlags[FLAG_LOWER_BOUND], argFlags[FLAG_UPPER_BOUND]);
                }
                else
                {
                        fp = popen("./sonarBCR.py", "r");
                        if(fp == NULL)
                        {
                                printf("-1\n");
                                exit(1);
                        }
                        
                        fgets(cmdOutput, sizeof(cmdOutput), fp);
                        pclose(fp);
                        distance = atoi(cmdOutput);
                }
                
                // Print the distance if it's between the bounds
                if(distance >= (argFlags[FLAG_LOWER_BOUND]) && distance <= (argFlags[FLAG_UPPER_BOUND]))
                {
                        printf("%d\n", distance);
                        
                        // Make a beep sound
                        if(argFlags[FLAG_DISPLAY])
                        {
                                sprintf(cmd, "./displayBCR.py 'Distance: %dcm'", distance);
                                system(cmd);
                        }
                }
                
                // Make a beep sound
                if(argFlags[FLAG_BUZZER])
                        system("./buzzerBCR.py");
                
                
                delay(argFlags[FLAG_DELAY]);
        }
        
        return 0;
}

// Function definitions
void printUsage(char* cmd)
{
	printf("USAGE:\n\t%s [-b/--buzzer] [-d/--delay <delay-amount>] -D/[--display] [--lower-bound <lower-bound>] [--upper-bound <upper-bound>] [-r/--random]\n\n\n", cmd);
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
		else if (strcmp(argv[i], "-D") == 0 || strcmp(argv[i], "--display") == 0)
		{
			if (argFlags[FLAG_DISPLAY])
			{
				argFlags[FLAG_ERR] = 1;
				break;
			}
			argFlags[FLAG_DISPLAY] = 1;  
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
		else if (strcmp(argv[i], "-l") == 0 || strcmp(argv[i], "--lower-bound") == 0)
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
		else if (strcmp(argv[i], "-u") == 0 || strcmp(argv[i], "--upper-bound") == 0)
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
	if (!argFlags[FLAG_LOWER_BOUND])
		argFlags[FLAG_LOWER_BOUND] = DEFAULT_LOWER_BOUND;
	if (!argFlags[FLAG_UPPER_BOUND])
		argFlags[FLAG_UPPER_BOUND] = DEFAULT_UPPER_BOUND;
}

float getRandomDistance(int lowerBound, int upperBound)
{
        return lowerBound + rand() / (RAND_MAX / (upperBound - lowerBound + 1) + 1);
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
        system("./buzzerBCR.py 0");
	system("./displayBCR.py");
	exit(0);
}