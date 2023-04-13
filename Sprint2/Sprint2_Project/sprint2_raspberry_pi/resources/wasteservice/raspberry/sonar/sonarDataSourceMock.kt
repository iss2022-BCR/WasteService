package wasteservice.raspberry.sonar

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import unibo.comm22.utils.CommUtils.delay

class sonarDataSourceMock(name: String): ActorBasic(name) {
    val distanceInitValue = 150
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
            readSonarData()
        }
        if(msg.msgId() == "sonardeactivate")
        {
            active = false
            println("[$name] Sonar deactivated.")
        }
    }

    private fun readSonarData()
    {
        println("[$name] Before reading")
        try {
            var distance = distanceInitValue

            // to allow message handling
            GlobalScope.launch {
                while (active) {
                    println("[$name] Sonar data: $distance")

                    val messageContent = "distance($distance)"
                    val event = MsgUtil.buildEvent(name, "sonardata", messageContent)
                    emitLocalStreamEvent(event)

                    distance -= 10
                    if(distance <= 0)
                        distance = distanceInitValue

                    delay(500)
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}