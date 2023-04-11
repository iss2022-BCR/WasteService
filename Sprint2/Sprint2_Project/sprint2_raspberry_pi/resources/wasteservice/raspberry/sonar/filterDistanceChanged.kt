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
        if(msg.msgId() != "sonar_data")
            return

        filterData(msg)
    }

    private suspend fun filterData(msg: IApplMessage)
    {
        val data  = (Term.createTerm(msg.msgContent()) as Struct).getArg(0).toString()
        val distance = Integer.parseInt(data)

        // Emit an event only if the distance changed
        if(distance != prevDistance)
        {
            msg.msgId()
            println("[$name] Distance: $distance")

            emit(msg)

            prevDistance = distance
        }
        else
        {
            println("[$name] Message discarded. PrevDistance: $prevDistance, distance: $distance")
        }
    }
}