/* Generated by AN DISI Unibo */ 
package it.unibo.warningdevice

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Warningdevice ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[WarningDevice] Initializing...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("state_update") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("updateled(LedStatus)"), Term.createTerm("update_led(ARG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("state_off") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t06",targetState="state_update",cond=whenDispatch("updateled"))
				}	 
				state("state_on") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t07",targetState="state_update",cond=whenDispatch("updateled"))
				}	 
				state("state_blinking") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t08",targetState="state_update",cond=whenDispatch("updateled"))
				}	 
			}
		}
}
