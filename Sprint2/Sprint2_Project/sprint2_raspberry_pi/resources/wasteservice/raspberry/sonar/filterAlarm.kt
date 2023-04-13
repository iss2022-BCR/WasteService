package wasteservice.raspberry.sonar

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct
import it.unibo.kactor.MsgUtil

/*
* Actor that filters a pipeline event and issues a stop/resume event,
* only when the state changes (from a prev state of < DLIM to >= DLIM
* and viceversa).
 */
class filterAlarm(name: String): ActorBasic(name) {
    var prevAlarm = false

    override suspend fun actorBody(msg: IApplMessage)
    {
        //println("[$name] Message: ${msg.toString()}")
        if(msg.msgSender() == name || msg.msgId() != "sonar_data")
            return

        val filteredMsg = filterData(msg)
        if(filteredMsg != null)
            emit(filteredMsg)
    }

    private suspend fun filterData(msg: IApplMessage): IApplMessage?
    {
        val data  = (Term.createTerm(msg.msgContent()) as Struct).getArg(0).toString()

        val distance = Integer.parseInt(data)

        val alarm = (distance < wasteservice.WSconstants.DLIM)
        // Emit an event only if the state changed
        if(alarm != prevAlarm) {
            prevAlarm = alarm
            println("[$name] Alarm: $alarm")

            val messageId = if (alarm) "stop" else "resume"

            return MsgUtil.buildEvent(name, messageId, "$messageId")
        }
        return null
    }
}