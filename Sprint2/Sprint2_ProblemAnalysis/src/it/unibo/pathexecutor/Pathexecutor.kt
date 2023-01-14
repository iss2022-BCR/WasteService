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
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 
				var CurMoveTodo = ""
				var MovesDone = "" 
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[PathExecutor] Started.")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("state_idle") { //this:State
					action { //it:State
						 CurMoveTodo = ""  
						 sysUtil.logMsgs=true  
						println("[PathExecutor] Waiting for a path...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t014",targetState="state_do_path",cond=whenRequest("dopath"))
					transition(edgeName="t015",targetState="state_handle_alarm",cond=whenEvent("startAlarm"))
				}	 
				state("state_do_path") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("dopath(PATH,OWNER)"), Term.createTerm("dopath(P,C)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val path = payloadArg(0); println(path)  
								 pathut.setPath(path)  
						}
						println("[PathExecutor] Path to do: ${pathut.getPathTodo()}")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_next_move", cond=doswitch() )
				}	 
				state("state_next_move") { //this:State
					action { //it:State
						 CurMoveTodo = pathut.nextMove()  
						 MovesDone += CurMoveTodo  
						println("[PathExecutor] Current move to do: $CurMoveTodo")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_end_work_ok", cond=doswitchGuarded({ CurMoveTodo.length == 0  
					}) )
					transition( edgeName="goto",targetState="state_do_move", cond=doswitchGuarded({! ( CurMoveTodo.length == 0  
					) }) )
				}	 
				state("state_do_move") { //this:State
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
					 transition( edgeName="goto",targetState="state_do_move_forward", cond=doswitchGuarded({ CurMoveTodo == "w"  
					}) )
					transition( edgeName="goto",targetState="state_do_move_turn", cond=doswitchGuarded({! ( CurMoveTodo == "w"  
					) }) )
				}	 
				state("state_do_move_forward") { //this:State
					action { //it:State
						request("step", "step(350)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t116",targetState="state_handle_alarm",cond=whenEvent("startAlarm"))
					transition(edgeName="t117",targetState="state_next_move",cond=whenReply("stepdone"))
					transition(edgeName="t118",targetState="state_end_work_fail",cond=whenReply("stepfail"))
				}	 
				state("state_do_move_turn") { //this:State
					action { //it:State
						forward("cmd", "cmd($CurMoveTodo)" ,"basicrobot" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		//sysaction { //it:State
				 	 		  stateTimer = TimerActor("timer_state_do_move_turn", 
				 	 			scope, context!!, "local_tout_pathexecutor_state_do_move_turn", 350.toLong() )
				 	 		//}
					}	 	 
					 transition(edgeName="t219",targetState="state_next_move",cond=whenTimeout("local_tout_pathexecutor_state_do_move_turn"))   
					transition(edgeName="t220",targetState="state_handle_alarm",cond=whenEvent("startAlarm"))
				}	 
				state("state_end_work_ok") { //this:State
					action { //it:State
						println("[PathExecutor] Path done.")
						answer("dopath", "dopathdone", "dopathdone(ok)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("state_end_work_fail") { //this:State
					action { //it:State
						 var PathStillTodo = pathut.getPathTodo()  
						println("[PathExecutor] Path failure. Path still to do: $PathStillTodo")
						answer("dopath", "dopathfail", "dopathfail($PathStillTodo)"   )  
						println("[PathExecutor] Out of service.")
						terminate(1)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("state_handle_alarm") { //this:State
					action { //it:State
						 var PathTodo = pathut.getPathTodo()  
						println("[PathExecutor] Alarm detected, stopped. Path to do: $PathTodo")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t321",targetState="state_resume",cond=whenDispatch("resume"))
				}	 
				state("state_resume") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_next_move", cond=doswitchGuarded({ CurMoveTodo.length > 0  
					}) )
					transition( edgeName="goto",targetState="state_idle", cond=doswitchGuarded({! ( CurMoveTodo.length > 0  
					) }) )
				}	 
			}
		}
}
