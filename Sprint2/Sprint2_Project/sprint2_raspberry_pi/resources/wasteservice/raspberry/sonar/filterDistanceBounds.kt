package wasteservice.raspberry.sonar

import alice.tuprolog.Struct
import alice.tuprolog.Term
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage
import it.unibo.kactor.MsgUtil
import wasteservice.WSconstants

class filterDistanceBounds(name: String): ActorBasic(name) {
    override suspend fun actorBody(msg: IApplMessage)
    {
        //println("[$name] Message: ${msg.toString()}")
        if(msg.msgSender() == name || msg.msgId() != "sonar_data")
            return

        filterData(msg)
    }

    private suspend fun filterData(msg: IApplMessage)
    {
        val data  = (Term.createTerm(msg.msgContent()) as Struct).getArg(0).toString()

        val distance = Integer.parseInt(data)

        // Emit an event only if the distance doesn't exceed the bounds
        if(distance >= WSconstants.SONAR_MIN && distance <= WSconstants.SONAR_MAX)
        {
            msg.msgId()
            //println("[$name] Distance: $distance")

            emit(msg)
        }
    }
}