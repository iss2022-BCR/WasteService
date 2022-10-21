package robotNano

import it.unibo.kactor.ActorBasic
import java.io.OutputStreamWriter

object nanoSupport {
    lateinit var writer : OutputStreamWriter

    fun create( owner : ActorBasic ){
        val p = Runtime.getRuntime().exec("sudo ./Motors")
        writer = OutputStreamWriter(  p.outputStream)
        println("motorscSupport | CREATED with writer=$writer")
    }

    fun move( msg : String="" ){
        //println("motorscSupport | WRITE $msg with $writer")
        writer.write( msg )
        writer.flush()
    }

    fun terminate(){

    }

}