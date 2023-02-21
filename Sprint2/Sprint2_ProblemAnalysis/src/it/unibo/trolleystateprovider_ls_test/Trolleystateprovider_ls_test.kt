/* Generated by AN DISI Unibo */ 
package it.unibo.trolleystateprovider_ls_test

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Trolleystateprovider_ls_test ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var TTState: wasteservice.TransportTrolleyState = wasteservice.TransportTrolleyState.HOME
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						CoapObserverSupport(myself, "localhost","11705","ctx_ledstate_test","transporttrolley_ls_test")
						CoapObserverSupport(myself, "localhost","11705","ctx_ledstate_test","pathexecutorbcr_ls_test")
						println("[TrolleyStateProvider] Started.")
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
					 transition(edgeName="t015",targetState="state_update_trolley_state",cond=whenDispatch("coapUpdate"))
					transition(edgeName="t016",targetState="state_update_trolley_state",cond=whenDispatch("update_trolley_state"))
				}	 
				state("state_update_trolley_state") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("coapUpdate(RESOURCE,VALUE)"), Term.createTerm("coapUpdate(RESOURCE,VALUE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  wasteservice.TransportTrolleyState.parseFromMessage(payloadArg(1)) != TTState  
								 ){ TTState = wasteservice.TransportTrolleyState.parseFromMessage(payloadArg(1))  
								println("[TrolleyStateProvider] New state. Resource: ${payloadArg(0)}; Value: ${TTState.name}")
								emit("trolley_state_changed", "trolley_state_changed($TTState)" ) 
								}
						}
						if( checkMsgContent( Term.createTerm("update_trolley_state(STATE)"), Term.createTerm("update_trolley_state(STATE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  wasteservice.TransportTrolleyState.parseFromMessage(payloadArg(1)) != TTState  
								 ){ TTState = wasteservice.TransportTrolleyState.parseFromMessage(payloadArg(1))  
								println("[TrolleyStateProvider] New state. Value: ${TTState.name}")
								emit("trolley_state_changed", "trolley_state_changed($TTState)" ) 
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
