package wasteservice.raspberry.sonar

import alice.tuprolog.Struct
import alice.tuprolog.Term
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage

/*
* Actor that filters a pipeline event by propagating it to the next actor
* in the stream, only when the distance detected has changed from the
* previous detection.
 */
class filterDistanceChanged (name : String ) : ActorBasic( name ) {
    var prevDistance: Int = -1

    override suspend fun actorBody(msg: IApplMessage)
    {
        if(msg.msgSender() == name || msg.msgId() != "sonar_data")
            return

        val filteredMsg = filterData(msg)
        if(filteredMsg != null)
            emitLocalStreamEvent(filteredMsg)
    }

    private suspend fun filterData(msg: IApplMessage): IApplMessage?
    {
        val data  = (Term.createTerm(msg.msgContent()) as Struct).getArg(0).toString()
        val distance = Integer.parseInt(data)

        // Emit an event only if the distance changed
        if(distance != prevDistance)
        {
            prevDistance = distance

            //println("[$name] Distance: $distance")

            return msg
        }
        return null
    }
}