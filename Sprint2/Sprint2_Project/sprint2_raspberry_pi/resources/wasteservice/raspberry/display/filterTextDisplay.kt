package wasteservice.raspberry.display

import alice.tuprolog.Struct
import alice.tuprolog.Term
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage
import wasteservice.raspberry.display.displaySupportBCR.doDisplay

/*
* Actor that filters a pipeline event and directly propagates it,
* updating the display with the distance.
 */
class filterTextDisplay(name: String): ActorBasic(name) {

    override suspend fun actorBody(msg: IApplMessage)
    {
        //println("[$name] Message: $msg")
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

        doDisplay("Distance: $distance cm", "")

        //println("[$name] Distance: $distance")

        return msg
    }
}