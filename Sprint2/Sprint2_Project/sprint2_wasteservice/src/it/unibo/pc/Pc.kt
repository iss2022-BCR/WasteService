/* Generated by AN DISI Unibo */ 
package it.unibo.pc

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Pc ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[PC] Waiting for messages")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="state_received_forward",cond=whenDispatch("forwardMessage"))
					transition(edgeName="t01",targetState="state_received_event",cond=whenEvent("eventMessage"))
				}	 
				state("state_received_forward") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("	[PC] Received a FORWARD message")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_reply", cond=doswitch() )
				}	 
				state("state_received_event") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("	[PC] Received an EVENT message")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_reply", cond=doswitch() )
				}	 
				state("state_reply") { //this:State
					action { //it:State
						forward("ack", "ack(_)" ,"rpi" ) 
						println("[PC] Sent ACK")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_init", cond=doswitch() )
				}	 
			}
		}
}