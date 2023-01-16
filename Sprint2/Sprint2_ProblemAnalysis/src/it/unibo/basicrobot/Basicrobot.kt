/* Generated by AN DISI Unibo */ 
package it.unibo.basicrobot

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Basicrobot ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
		  var StepTime      = 0L
		  var StartTime     = 0L     
		  var Duration      = 0L  
		  var RobotType     = "" 
		  var CurrentMove   = "unkknown"
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						discardMessages = false
						println("basicrobot | START")
						unibo.robot.robotSupport.create(myself ,"basicrobotConfig.json" )
						 RobotType = unibo.robot.robotSupport.robotKind  
						delay(1000) 
						if(  RobotType != "virtual"  
						 ){ var robotsonar = context!!.hasActor("realsonar")  
						        	   if(robotsonar != null) unibo.robot.robotSupport.createSonarPipe(robotsonar) 
						}
						updateResourceRep( "basicrobot(start)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						println("basicrobot  | waiting .................. ")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t123",targetState="execcmd",cond=whenDispatch("cmd"))
					transition(edgeName="t124",targetState="doStep",cond=whenRequest("step"))
					transition(edgeName="t125",targetState="handleObstacle",cond=whenDispatch("obstacle"))
					transition(edgeName="t126",targetState="endwork",cond=whenDispatch("end"))
				}	 
				state("execcmd") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("cmd(MOVE)"), Term.createTerm("cmd(MOVE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentMove = payloadArg(0)  
								unibo.robot.robotSupport.move( payloadArg(0)  )
								updateResourceRep( "moveactivated(${payloadArg(0)})"  
								)
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handleObstacle") { //this:State
					action { //it:State
						updateResourceRep( "obstacle(${CurrentMove})"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handleSonar") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("doStep") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("step(TIME)"), Term.createTerm("step(T)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									StepTime = payloadArg(0).toLong() 	 
								updateResourceRep( "step(${StepTime})"  
								)
						}
						StartTime = getCurrentTime()
						println("basicrobot | doStep StepTime =$StepTime  ")
						unibo.robot.robotSupport.move( "w"  )
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		//sysaction { //it:State
				 	 		  stateTimer = TimerActor("timer_doStep", 
				 	 			scope, context!!, "local_tout_basicrobot_doStep", StepTime )
				 	 		//}
					}	 	 
					 transition(edgeName="t027",targetState="stepDone",cond=whenTimeout("local_tout_basicrobot_doStep"))   
					transition(edgeName="t028",targetState="stepFail",cond=whenDispatch("obstacle"))
				}	 
				state("stepDone") { //this:State
					action { //it:State
						unibo.robot.robotSupport.move( "h"  )
						updateResourceRep( "stepDone($StepTime)"  
						)
						answer("step", "stepdone", "stepdone(ok)"   )  
						println("basicrobot | stepDone reply done")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("stepFail") { //this:State
					action { //it:State
						Duration = getDuration(StartTime)
						unibo.robot.robotSupport.move( "h"  )
						 var TunedDuration   =  ((StepTime - Duration) * 15 / 100).toLong()    
						println("basicrobot | stepFail duration=$Duration TunedDuration=$TunedDuration")
						unibo.robot.robotSupport.move( "s"  )
						delay(TunedDuration)
						unibo.robot.robotSupport.move( "h"  )
						updateResourceRep( "stepFail($Duration)"  
						)
						answer("step", "stepfail", "stepfail($Duration,obst)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("endwork") { //this:State
					action { //it:State
						updateResourceRep( "basicrobot(end)"  
						)
						terminate(1)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
}
