/* Generated by AN DISI Unibo */ 
package it.unibo.pathexecutorbcr

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Pathexecutorbcr ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 
				var CurMoveTodo = ""
				var MovesDone = ""
				var TotPathMoves = 0
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[PathExecutorBCR] Started.")
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
									TotPathMoves = 0
									sysUtil.logMsgs = true
						println("[PathExecutorBCR] Waiting for a path...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t014",targetState="state_do_path",cond=whenRequest("dopath"))
					transition(edgeName="t015",targetState="state_stop",cond=whenEvent("stop"))
				}	 
				state("state_do_path") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("dopath(PATH,OWNER)"), Term.createTerm("dopath(P,C)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												val path = payloadArg(0)
												pathut.setPath(path)
												MovesDone = ""
												TotPathMoves = pathut.getPathTodo().length
								println("$path")
						}
						println("[PathExecutorBCR] Path to do: ${pathut.getPathTodo()}")
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
						println("[PathExecutorBCR] Current move to do: $CurMoveTodo")
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
						
									val X = plannerBCR.get_curPos().first
									val Y = plannerBCR.get_curPos().second
						forward("update_trolley_position", "update_trolley_position($X,$Y)" ,"gui_controller" ) 
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
						delay(350) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t116",targetState="state_stop",cond=whenEvent("stop"))
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
				 	 			scope, context!!, "local_tout_pathexecutorbcr_state_do_move_turn", 350.toLong() )
				 	 		//}
					}	 	 
					 transition(edgeName="t219",targetState="state_next_move",cond=whenTimeout("local_tout_pathexecutorbcr_state_do_move_turn"))   
					transition(edgeName="t220",targetState="state_stop",cond=whenEvent("stop"))
				}	 
				state("state_end_work_ok") { //this:State
					action { //it:State
						 MovesDone = ""  
						println("[PathExecutorBCR] Path done.")
						answer("dopath", "dopathdone", "dopathdone(ok)","transporttrolley"   )  
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
						println("[PathExecutorBCR] Path failure. Path still to do: $PathStillTodo")
						answer("dopath", "dopathfail", "dopathfail($PathStillTodo)","transporttrolley"   )  
						println("[PathExecutorBCR] Out of service.")
						terminate(1)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("state_stop") { //this:State
					action { //it:State
						updateResourceRep( "transporttrolley(STOPPED)"  
						)
						 var PathTodo = pathut.getPathTodo()  
						println("[PathExecutorBCR] Alarm detected, trolley stopped. Path to do: $PathTodo")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t321",targetState="state_resume",cond=whenEvent("resume"))
				}	 
				state("state_resume") { //this:State
					action { //it:State
						if(  MovesDone.length != TotPathMoves  
						 ){updateResourceRep( "transporttrolley(MOVING)"  
						)
						}
						else
						 {updateResourceRep( "transporttrolley(HOME)"  
						 )
						 }
						println("[PathExecutorBCR] Alarm retracted. Resuming...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_next_move", cond=doswitchGuarded({ MovesDone.length != TotPathMoves  
					}) )
					transition( edgeName="goto",targetState="state_idle", cond=doswitchGuarded({! ( MovesDone.length != TotPathMoves  
					) }) )
				}	 
			}
		}
}
