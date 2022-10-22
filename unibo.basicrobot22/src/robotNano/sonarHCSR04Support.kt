package robotNano
/*
 -------------------------------------------------------------------------------------------------
 Run SonarAlone and reads data from its output
 For each data value V, it emitLocalStreamEvent sonarRobot:sonar(V)
 -------------------------------------------------------------------------------------------------
 */

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

object sonarHCSR04Support {
    lateinit var reader : BufferedReader

    //g++  SonarAlone.c -l wiringPi -o  SonarAlone
    fun create( actor : ActorBasic, todo : String="" ){
        println("sonarHCSR04Support CREATING for ${actor.name}")
        val p = Runtime.getRuntime().exec("sudo ./SonarAlone")
        reader = BufferedReader(  InputStreamReader(p.inputStream))
        startRead( actor )
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun startRead( actor: ActorBasic  ){
        GlobalScope.launch{
            while( true ){
                val data = withContext(Dispatchers.IO) {
                    reader.readLine()
                }
                //println("sonarHCSR04Support data = $data"   )
                if( data != null ){
                    val m1 = "sonar( $data )"
                    val event = MsgUtil.buildEvent( "sonarsupport","sonarRobot",m1)
                    //println("sonarHCSR04Support event = $event actor=${actor.name}"   )
                    //actor.emit(  event )		//AVOID: better use a stream
                    //(streaming)
                    actor.emitLocalStreamEvent( event )
                }
                //delay( 100 )
            }
        }
    }
}