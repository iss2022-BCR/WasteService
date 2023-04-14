package wasteservice.raspberry.sonar

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage
import it.unibo.kactor.MsgUtil
import it.unibo.radarSystem22.domainBCR.interfaces.ISonar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import `it.unibo`.radarSystem22.domainBCR.*
import `it.unibo`.radarSystem22.domainBCR.utils.*

class sonarSupportBCR(name: String): ActorBasic(name) {
    lateinit var sonar : ISonar

    init
    {
        println("[$name] Started.")
        DomainSystemConfig.setTheConfiguration("RaspberryDomainConfig.json")
        sonar = DeviceFactory.createSonar()
    }

    override suspend fun actorBody(msg: IApplMessage)
    {
        if(msg.msgId() == "sonaractivate")
        {
            sonar.activate()
            println("[$name] Sonar activated.")
            readDistance()
        }
        if(msg.msgId() == "sonardeactivate")
        {
            sonar.deactivate()
            println("[$name] Sonar deactivated.")
        }
    }

    private fun readDistance()
    {
        //var prevData: Int = sonar.distance.`val`
        var data: Int = -1

        GlobalScope.launch {	// to allow message handling
            while(sonar.isActive)
            {
                // Read the distance from the sonar component
                data = sonar.distance.`val`

                //println("[$name] data: $data")

                val content = "distance($data)"
                val event = MsgUtil.buildEvent(name, "sonar_data", content)
                emitLocalStreamEvent(event)		//not propagated to remote actors

                delay(DomainSystemConfig.sonarDelay.toLong()) // avoid too fast generation
            }
        }
    }
}