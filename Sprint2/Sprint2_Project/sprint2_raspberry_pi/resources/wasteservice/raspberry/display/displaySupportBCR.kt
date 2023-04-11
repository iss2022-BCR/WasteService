package wasteservice.raspberry.display

import unibo.comm22.utils.ColorsOut
import wasteservice.raspberry.led.ledSupportBCR
import java.io.IOException

object displaySupportBCR {
    private var prevLine1: String = ""
    private var prevLine2: String = ""

    fun writeToDisplay(line1: String, line2: String)
    {
        var newLine1: String
        var newLine2: String

        // Update a line only if the new one is not empty/blank
        if(line1.isNotBlank())
        {
            prevLine1 = line1
        }
        if(line2.isNotBlank())
        {
            prevLine2 = line2
        }
        // Clear the display if both lines are empty/blank
        if(line1.isBlank() && line2.isBlank())
        {
            prevLine1 = ""
            prevLine2 = ""
        }

        val builder = ProcessBuilder("/usr/bin/python3", "-u", "./displayBCR.py", prevLine1, prevLine2)
        builder.redirectErrorStream(true)

        try {
            builder.start()
        }
        catch (e: IOException) {
            ColorsOut.outappl("An error occurred while starting the display program.\n${e.toString()}", unibo.comm22.utils.ColorsOut.RED)
            e.printStackTrace()
        }
    }

    fun clearDisplay()
    {
        val builder = ProcessBuilder("/usr/bin/python3", "-u", "./displayBCR.py")
        builder.redirectErrorStream(true)

        try {
            builder.start()
        }
        catch (e: IOException) {
            ColorsOut.outappl("An error occurred while starting the display program.\n${e.toString()}", unibo.comm22.utils.ColorsOut.RED)
            e.printStackTrace()
        }
    }
}