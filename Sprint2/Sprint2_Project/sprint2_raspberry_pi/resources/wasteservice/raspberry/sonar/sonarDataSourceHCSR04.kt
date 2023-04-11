package wasteservice.raspberry.sonar

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import unibo.comm22.utils.ColorsOut
import unibo.comm22.utils.CommUtils.delay
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class sonarDataSourceHCSR04(name: String): ActorBasic(name) {
    lateinit var process: Process
    lateinit var `in`: BufferedReader
    var active: Boolean = false

    init
    {
        println("[$name] Started.")
    }

    override suspend fun actorBody(msg: IApplMessage)
    {
        if(msg.msgId() == "sonaractivate")
        {
            active = true
            println("[$name] Sonar activated.")
            runSonarScript()
            readSonarData()
        }
        if(msg.msgId() == "sonardeactivate")
        {
            active = false
            println("[$name] Sonar deactivated.")
            stopSonarScript()
        }
    }

    private fun runSonarScript()
    {
        val builder = ProcessBuilder("/usr/bin/python3", "-u", "./sonarBCR.py", "loop", "500")
        //val builder = ProcessBuilder("./SonarAlone")
        builder.redirectErrorStream(true)

        try {
            process = builder.start()
            `in` = BufferedReader(InputStreamReader(process.inputStream))
        }
        catch (e: IOException) {
            ColorsOut.outappl("[$name] An error occurred while starting the sonar program.\n${e.toString()}", unibo.comm22.utils.ColorsOut.RED)
            e.printStackTrace()
        }
    }

    private fun stopSonarScript()
    {
        if(this::process.isInitialized && process.isAlive)
        {
            process.destroy()
            `in`.close()
        }
    }

    private fun readSonarData()
    {
        println("[$name] Before reading")
        try {
            var distance: Integer

            // to allow message handling
            GlobalScope.launch {
                while (active) {
                    // if the sonar script output is not ready wait 100ms
                    if (!`in`.ready())
                    {
                        delay(50)
                    }
                    else
                    {
                        val line = `in`.readLine();
                        val distance = line.toInt()

                        //println("[$name] Sonar data: $distance")

                        val messageContent = "distance($distance)"
                        val event = MsgUtil.buildEvent(name, "sonar_data", messageContent)
                        emitLocalStreamEvent(event)
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}