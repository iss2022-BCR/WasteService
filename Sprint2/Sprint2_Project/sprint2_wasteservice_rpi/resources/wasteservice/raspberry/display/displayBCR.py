#!/usr/bin/python3

# Usage: ./displayBCR.py <text-for-line-1> <text-for-line-2> 

from rpi_lcd import LCD
import sys

# MAIN =================================
if __name__ == "__main__":
        try:
                lcd = LCD()

                if len(sys.argv) == 1 or len(sys.argv) > 3:
                        lcd.clear()
                        exit(0)

                for i in range(1, len(sys.argv)):
                        lcd.text(str(sys.argv[i]), i)
        except:
                pass