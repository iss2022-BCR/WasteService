#include <stdio.h>
#include <windows.h>
#include <time.h>
#include <stdlib.h>

int main()
{
	srand(time(NULL));
	
	while(1)
	{
		int distance = rand() % 300;
		printf("%d\n", distance);
		
		Sleep(1000); // Note uppercase S
	}
	
	return 0;
}