 /*
 pathutil.kt
 */

import org.json.JSONObject
import it.unibo.kactor.*
import java.util.Scanner
import alice.tuprolog.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

import java.io.PrintWriter
import java.io.FileWriter
import java.io.ObjectOutputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.FileInputStream
import unibo.planner22.model.*

object pathut{
	var pathDone = ""
	var curMove  = "unknown"	
	var curPath  = ""
	var wenvAddr = "localhost"
	lateinit var  master: ActorBasicFsm
	lateinit var  owner:  String
	
	
	fun setPath(path: String)  {
 		//path : "wwlw"  
		curPath = path
	}
		
	fun setPathFromRequest(msg: IApplMessage)  {
		var payload     = msg.msgContent().replace("'","")
		//payload : {"path":"wwlw" , "owner":"pathexecCaller"}
		curPath = JSONObject(payload).getString("path")
	}
	
	fun getPathTodo() : String{
		return curPath
	}

	fun nextMove() : String{
		//println("pathexecutil | nextMove curPath=$curPath")
		if( curPath.length == 0 ) return ""
		//curPath still has moves
		val move = ""+curPath[0]
		curPath  = curPath.substring(1)
		return move
	}
	
 	fun waitUser(prompt: String) {
		print(">>>  $prompt >>>  ")
		val scanner = Scanner(System.`in`)
		try {
			scanner.nextInt()
		} catch (e: java.lang.Exception) {
			e.printStackTrace()
		}
	}
	
	fun saveAdjustedMap( fname : String ){
		val adjustedMap = RoomMap.getRoomMap().toString().replace("r,","1,")
		val pw = PrintWriter( FileWriter(fname+".txt") )
		pw.print( adjustedMap )
		pw.close()
		
		val os = ObjectOutputStream( FileOutputStream(fname+".bin") )
		os.writeObject( RoomMap.getRoomMap() )
		os.flush()
		os.close()		
	}
	
	fun test(){
		//val map = RoomMap("map2019.txt")
	}
}