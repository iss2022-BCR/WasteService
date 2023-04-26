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

	suspend fun doNextMove( master: ActorBasicFsm) {
		val move = nextMove()
		//println("pathexecutil | doNextMove=$move")
		delay(150)  	//to reduce path speed
		if( move.length == 0 ) {
			//master.autoMsg("pathdone","pathtodo($curPath)")
			//println("!!!!!!!!!!! SEND pathdone to OWNER=$owner" )
		}else{
			doMove(master, move)
		}
	}
		
	fun nextMove() : String{
		//println("pathexecutil | nextMove curPath=$curPath")
		if( curPath.length == 0 ) return ""
		//curPath still has moves
		val move = ""+curPath[0]
		curPath  = curPath.substring(1)
		return move
	}



	suspend fun doMove(master: ActorBasicFsm, moveTodo: String ){
		println("pathexecutil | doMove moveTodo=$moveTodo")
/*		
 //robot.send(ApplMsgs.stepRobot_step("appl", "350"))
 //support.//
		//val MoveAnsw = CallRestWithApacheHTTP.doMove(moveTodo)
		curMove = moveTodo
		when( curMove ){
			"p" -> robot.send(ApplMsgs.stepRobot_step("appl", "350"))
			"l" -> robot.send(ApplMsgs.stepRobot_l("appl"))
			"r" -> robot.send(ApplMsgs.stepRobot_r("appl"))
			else -> println("$curMove uknown")
		}
 */
		
		/*
		println("pathexecutil | doMove $moveTodo MoveAnsw=$MoveAnsw")
 
		val answJson = JSONObject( MoveAnsw ) 
		//println("pathexecutil | doMove $moveTodo answJson=$answJson")
		if( ( answJson.has("endmove") && answJson.getString("endmove") == "true")
			|| answJson.has("stepDone") ){
			pathDone = pathDone+moveTodo
			master.autoMsg("moveok","move($moveTodo)")
		}else{
			master.autoMsg("pathfail","pathdone($pathDone)")
			//println("!!!!!!!!!!!  SEND pathfail to OWNER=$owner")
		}*/
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
}