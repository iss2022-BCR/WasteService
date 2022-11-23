/* Generated by AN DISI Unibo */ 
package it.unibo.pathexecutor

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Pathexecutor ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 
				var CurMoveTodo = ""
				var MovesDone = "" 
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						 CurMoveTodo = ""  
						 sysUtil.logMsgs=true  
						println("[PathExecutor] started")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t015",targetState="doThePath",cond=whenRequest("dopath"))
				}	 
				state("doThePath") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("dopath(PATH,OWNER)"), Term.createTerm("dopath(P,C)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val path = payloadArg(0); println(path)  
								 pathut.setPath(path)  
						}
						println("[PathExecutor] pathTodo: ${pathut.getPathTodo()}")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="nextMove", cond=doswitch() )
				}	 
				state("nextMove") { //this:State
					action { //it:State
						 CurMoveTodo = pathut.nextMove()  
						 MovesDone += CurMoveTodo  
						println("[PathExecutor] curMoveTodo: $CurMoveTodo")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="endWorkOk", cond=doswitchGuarded({ CurMoveTodo.length == 0  
					}) )
					transition( edgeName="goto",targetState="doMove", cond=doswitchGuarded({! ( CurMoveTodo.length == 0  
					) }) )
				}	 
				state("doMove") { //this:State
					action { //it:State
						
									plannerBCR.updateMap(CurMoveTodo, "")
									// plannerBCR.showMap()
									// plannerBCR.showCurrentRobotState()	
						delay(350) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="doMoveW", cond=doswitchGuarded({ CurMoveTodo == "w"  
					}) )
					transition( edgeName="goto",targetState="doMoveTurn", cond=doswitchGuarded({! ( CurMoveTodo == "w"  
					) }) )
				}	 
				state("doMoveTurn") { //this:State
					action { //it:State
						forward("cmd", "cmd($CurMoveTodo)" ,"basicrobot" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		//sysaction { //it:State
				 	 		  stateTimer = TimerActor("timer_doMoveTurn", 
				 	 			scope, context!!, "local_tout_pathexecutor_doMoveTurn", 350.toLong() )
				 	 		//}
					}	 	 
					 transition(edgeName="t116",targetState="nextMove",cond=whenTimeout("local_tout_pathexecutor_doMoveTurn"))   
				}	 
				state("doMoveW") { //this:State
					action { //it:State
						request("step", "step(350)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t217",targetState="handleAlarm",cond=whenEvent("alarm"))
					transition(edgeName="t218",targetState="nextMove",cond=whenReply("stepdone"))
					transition(edgeName="t219",targetState="endWorkKo",cond=whenReply("stepfail"))
				}	 
				state("handleAlarm") { //this:State
					action { //it:State
						 var PathTodo = pathut.getPathTodo()  
						println("[PathExecutor] handleAlarm ... pathTodo: $PathTodo")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("endWorkOk") { //this:State
					action { //it:State
						println("[PathExecutor] Path done - bye")
						answer("dopath", "dopathdone", "dopathdone(ok)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="init", cond=doswitch() )
				}	 
				state("endWorkKo") { //this:State
					action { //it:State
						 var PathStillTodo = pathut.getPathTodo()  
						println("[PathExecutor] path failure - sorry. PathStillTodo: $PathStillTodo")
						answer("dopath", "dopathfail", "dopathfail($PathStillTodo)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t020",targetState="handleAlarm",cond=whenEvent("alarm"))
				}	 
			}
		}
}
