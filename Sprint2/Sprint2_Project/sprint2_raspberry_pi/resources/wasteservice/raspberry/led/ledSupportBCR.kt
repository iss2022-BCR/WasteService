package wasteservice.raspberry.led

import unibo.comm22.utils.ColorsOut
import wasteservice.LedState
import java.io.IOException

object ledSupportBCR {
    private lateinit var process: Process

    private fun runProgram(vararg command: String)
    {
        if(this::process.isInitialized && process.isAlive)
            process.destroy()

        val builder = ProcessBuilder(*command)
        builder.redirectErrorStream(true)

        try {
            process = builder.start()
        }
        catch (e: IOException) {
            ColorsOut.outappl("An error occurred while starting the led program.\n${e.toString()}", unibo.comm22.utils.ColorsOut.RED)
            e.printStackTrace()
        }
    }

    fun turnOnLed()
    {
        val command = arrayOf("/usr/bin/python3", "-u", "./ledBCR.py", "on")
        runProgram(*command)
    }

    fun turnOffLed()
    {
        val command = arrayOf("/usr/bin/python3", "-u", "./ledBCR.py", "off")
        runProgram(*command)
    }

    fun blinkLed(delay: Int)
    {
        val command = arrayOf("/usr/bin/python3", "-u", "./ledBCR.py", "blink", "$delay")
        runProgram(*command)
    }
    fun blinkLed()
    {
        blinkLed(100)
    }

    fun doLed(state: LedState)
    {
        if(state == LedState.ON)
            turnOnLed()
        else if(state == LedState.OFF)
            turnOffLed()
        else if(state == LedState.BLINKING)
            blinkLed()
    }
}