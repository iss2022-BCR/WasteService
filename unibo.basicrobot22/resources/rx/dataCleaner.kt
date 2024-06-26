package rx

import alice.tuprolog.Term
import alice.tuprolog.Struct
import it.unibo.kactor.*

 
class dataCleaner (name : String ) : ActorBasic( name ) {
val LimitLow  = 2	
val LimitHigh = 150


    override suspend fun actorBody(msg: IApplMessage) {
  		//println("$tt $name |  msg = $msg ")
		if( msg.msgId() != "sonarRobot" ) return //AVOID to handle other events
  		elabData( msg )
 	}

 	


	  suspend fun elabData( msg: IApplMessage ){ //OPTIMISTIC		 
 		val data  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
  		//println("$tt $name |  data = $data ")
		val Distance = Integer.parseInt( data ) 
 		if(Distance in (LimitLow + 1) until LimitHigh){
			emitLocalStreamEvent( msg ) //propagate
     	}else{
			println("$tt $name |  DISCARDS $Distance ")
 		}				
 	}
}