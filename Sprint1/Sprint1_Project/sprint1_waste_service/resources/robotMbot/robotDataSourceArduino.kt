package robotMbot
/*
 -------------------------------------------------------------------------------------------------
 Reads data from the serial connection to Arduino
 For each data value V, it emitLocalStreamEvent sonarRobot:sonar(V)
 -------------------------------------------------------------------------------------------------
 */
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.utils.CommUtils


class robotDataSourceArduino( name : String, owner : ActorBasic ,
					val conn : Interaction2021) : ActorBasic(name, owner.scope){
	
companion object {
	const val eventId = "sonarRobot"
}		
	init{
 		println("   	%%% $name |  starts conn=$conn")	 
 	}

	override suspend fun actorBody(msg: IApplMessage) {
        //println("   	%%% $name |  handles msg= $msg  ")
		//val vStr  = (Term.createTerm( msg.msgContent()) as Struct).getArg(0).toString()
		//println("   	%%% $name |  handles msg= $msg  vStr=$vStr")
		elabData(   )
	}

	suspend fun elabData(  ){
		var counter   = 0
		println("   	%%% $name |  elabData ... on $conn")
		GlobalScope.launch{	//to allow message handling
			while (true) {
			try {
 				val curDataFromArduino = conn.receiveMsg()
   				val v = curDataFromArduino.toDouble()
   				val dataSonar = v.toInt()
                //println("   	%%% $name | elabData: $dataSonar | ${counter++}"    )

 				val event = CommUtils.buildEvent( name,"sonarRobot","sonar( $dataSonar )")
  				//println("   	%%% $name | robotDataSourceArduino event: ${ event } owner=${owner.name}"   );
				//owner.emit(  event )
				emitLocalStreamEvent( event )
				//delay(100)  //WARNING: if included, it does not change values
			} catch ( e : Exception) {
 				println("   	%%% $name | robotDataSourceArduino | ERROR $e   ")
            }
 			//delay(100)  //WARNING: if included, it does read correct values
			}//while
		}//launch
 	}
	
 
}