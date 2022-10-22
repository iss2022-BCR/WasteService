package robotNano

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader

/*
 Emits the event sonarRobot : sonar( V )
 */
class sonarHCSR04SupportActor ( name : String ) : ActorBasic( name ) {
    lateinit var reader : BufferedReader

    init{
        println("$tt $name | CREATING")
    }
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg : IApplMessage){
        println("$tt $name | received  $msg "  )
        if( msg.msgId() == "sonarstart"){
            println("$tt $name | STARTING")
            try{
                val p = withContext(Dispatchers.IO) {
                    Runtime.getRuntime().exec("sudo ./SonarAlone")
                }
                reader = BufferedReader(  InputStreamReader(p.inputStream))
                startRead(   )
            }catch( e : Exception){
                println("$tt $name | WARNING:  does not find SonarAlone")
            }
        }
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    suspend fun startRead(   ){
        var counter = 0
        GlobalScope.launch{	//to allow message handling
            while( true ){
                val data = withContext(Dispatchers.IO) {
                    reader.readLine()
                }
                //println("sonarHCSR04Support data = $data"   )
                if( data != null ){
                    try{
                        val v = data.toInt()
                        if( v <= 150 ){	//A first filter ...
                            val m1 = "sonar( $v )"
                            val event = MsgUtil.buildEvent( name,"sonarRobot",m1)
                            //emit( event )
                            emitLocalStreamEvent( event )		//not propagated to remote actors
                            println("$tt $name |  ${counter++}: $event "   )
                        }
                    }catch(_: Exception){}
                }
                delay( 100 )
            }
        }
    }
}