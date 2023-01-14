/* Generated by AN DISI Unibo */ 
package it.unibo.alarm_controller

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Alarm_controller ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var SonarDistance: Double = Double.MAX_VALUE
				var PrevAlarm: Boolean = false
				var Alarm: Boolean = false
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[AlarmController] Started.")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("state_idle") { //this:State
					action { //it:State
						println("[AlarmController] Listening for sonar data...")
						 PrevAlarm = Alarm  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t024",targetState="state_handle_sonar_data",cond=whenDispatch("sonar_data"))
				}	 
				state("state_handle_sonar_data") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("sonar_data(DISTANCE,TIMESTAMP)"), Term.createTerm("sonarevent(DISTANCE,TIMESTAMP)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 Alarm = wasteservice.Utils.isAlarm(payloadArg(0))  
								println("[AlarmController] Received sonar data: ${payloadArg(0)}")
								if(  Alarm != PrevAlarm  
								 ){if(  Alarm == true  
								 ){println("[AlarmController] Alarm situation!")
								emit("startAlarm", "startAlarm(_)" ) 
								}
								else
								 {println("[AlarmController] Alarm over.")
								 emit("stopAlarm", "stopAlarm(_)" ) 
								 }
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
			}
		}
}
