/* Generated by AN DISI Unibo */ 
package it.unibo.buzzer_bcr

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Buzzer_bcr ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var TTState: wasteservice.TransportTrolleyState = wasteservice.TransportTrolleyState.HOME
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[BuzzerBCR] Started.")
						wasteservice.raspberry.buzzer.buzzerSupportBCR.createBuzzer(  )
						wasteservice.raspberry.buzzer.buzzerSupportBCR.doBuzzer( TTState  )
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("state_idle") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="state_handle",cond=whenEvent("trolley_state_changed"))
				}	 
				state("state_handle") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("trolley_state_changed(STATE)"), Term.createTerm("trolley_state_changed(STATE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												val TTState = wasteservice.TransportTrolleyState.parseFromMessage(payloadArg(0))
												wasteservice.raspberry.buzzer.buzzerSupportBCR.doBuzzer(TTState)
								println("[BuzzerBCR] State: $TTState")
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
