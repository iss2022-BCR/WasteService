package wasteservice.sonarTestWindows

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class sonarWindows(name: String): ActorBasic(name) {
    val nameTmp = "SonarWindows"
    lateinit var reader: BufferedReader

    override suspend fun actorBody(msg: IApplMessage) {
        println("[$nameTmp] Started.")
        try {
            //val builder = ProcessBuilder("./sonarFakeCringe.bat")
            //val builder = ProcessBuilder("/usr/bin/python3", "./sonarBCR.py", "loop", "500")
            val builder = ProcessBuilder("./SonarAlone") // NB: con sudo non funziona
            builder.redirectErrorStream(true)
            val process = builder.start()
            val `in` = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (true) {
                // if the sonar script output is not ready wait 500ms
                if (!`in`.ready()) Thread.sleep(500) else {
                    println("[$nameTmp] reading...")
                    line = `in`.readLine();
                    println("[$nameTmp] data: $line");
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun startRead(){
        println("[SonarWindows] startRead")
        var counter = 0
        GlobalScope.launch{	//to allow message handling
            while(true){
                var data = reader.readLine()
                println("[sonarHCSR04Support] data = $data")
                if( data != null ){
                    try{
                        val v = data.toInt()
                        if( v <= 150 ){	//A first filter ...
                            val m1 = "distance($v)"
                            val event = MsgUtil.buildEvent( name,"sonar",m1)
                            emitLocalStreamEvent( event )		//not propagated to remote actors
                            //println("sonarEmitterConcrete | ${counter++}: $event "   )
                        }
                    }catch(e: Exception){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}