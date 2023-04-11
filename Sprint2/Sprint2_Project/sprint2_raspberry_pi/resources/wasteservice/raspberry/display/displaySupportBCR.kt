package wasteservice.raspberry.display

import unibo.comm22.utils.ColorsOut
import wasteservice.raspberry.led.ledSupportBCR
import java.io.IOException

object ledSupportBCR {
    private var prevLine1: String = ""
    private var prevLine2: String = ""

    fun writeToDisplay(line1: String, line2: String)
    {
        val builder = ProcessBuilder("/usr/bin/python3", "-u", "./displayBCR.py", line1, line2)
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

    fun doDisplay()
    {

    }
}