//package rx
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.IApplMessage
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking

/*
-------------------------------------------------------------------------------------------------
 
-------------------------------------------------------------------------------------------------
 */

class sonarSimulator ( name : String ) : ActorBasic( name ) {
	var goon = true
	val data = sequence<Int>{
		var v0 = 80
		yield(v0)
		while(true){
			v0 = v0 - 5
			yield( v0 )
		}
	}

    override suspend fun actorBody(msg : IApplMessage){
  		//println("$tt $name | received  $msg "  )  //RICEVE GLI EVENTI!!!
		if( msg.msgId() == "sonaractivate") startDataReadSimulation(   )
		if( msg.msgId() == "sonardeactivate") goon=false
	}

	suspend fun startDataReadSimulation(    ){
  			var i = 0
			while( i < 10 && goon ){
 	 			val m1 = "distance( ${data.elementAt(i*2)} )"
				i++
 				val event = MsgUtil.buildEvent( name,"sonar",m1)								
  				emitLocalStreamEvent( event )
 				//println("$tt $name | generates $event")
 				//emit(event)  //APPROPRIATE ONLY IF NOT INCLUDED IN A PIPE
 				delay( 500 )
  			}			
			terminate()
	}

} 

