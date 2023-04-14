package wasteservice.raspberry.sonar

import alice.tuprolog.Struct
import alice.tuprolog.Term
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig

class filterDistanceBounds(name: String): ActorBasic(name) {
    override suspend fun actorBody(msg: IApplMessage)
    {
        //println("[$name] Message: $msg")
        if(msg.msgSender() == name || msg.msgId() != "sonar_data")
            return

        val filteredMsg = filterData(msg)
        if(filteredMsg != null)
            emitLocalStreamEvent(msg)
    }

    private suspend fun filterData(msg: IApplMessage): IApplMessage?
    {
        val data  = (Term.createTerm(msg.msgContent()) as Struct).getArg(0).toString()

        val distance = Integer.parseInt(data)

        // Emit an event only if the distance doesn't exceed the bounds
        if(distance >= DomainSystemConfig.sonarDistanceMin &&
            distance <= DomainSystemConfig.sonarDistanceMax)
        {
            //println("[$name] Distance: $distance")

            return msg
        }
        return null
    }
}