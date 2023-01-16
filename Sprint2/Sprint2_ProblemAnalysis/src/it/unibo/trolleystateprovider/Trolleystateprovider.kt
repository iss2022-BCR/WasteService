/* Generated by AN DISI Unibo */ 
package it.unibo.trolleystateprovider

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Trolleystateprovider ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var PrevState: wasteservice.TransportTrolleyState = wasteservice.TransportTrolleyState.values()[0]
				var State: wasteservice.TransportTrolleyState = wasteservice.TransportTrolleyState.values()[0]
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
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
						println("[TrolleyStateProvider] Idle...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t012",targetState="state_update_trolley_state",cond=whenDispatch("updatetrolleystate"))
					transition(edgeName="t013",targetState="state_handle_prev_state_request",cond=whenRequest("requesttrolleyprevstate"))
					transition(edgeName="t014",targetState="state_handle_state_request",cond=whenRequest("requesttrolleystate"))
				}	 
				state("state_update_trolley_state") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("updatetrolleystate(STATE)"), Term.createTerm("updatetrolleystate(STATE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												PrevState = State
												State = wasteservice.TransportTrolleyState.valueOf(payloadArg(0))
								println("[TrolleyStateProvider] State updated. Prev: $PrevState, State: $State")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("state_handle_state_request") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("state_handle_prev_state_request") { //this:State
					action { //it:State
						println("[TrolleyStateProvider] Received prev state request, replied with: $PrevState")
						if(  State == wasteservice.TransportTrolleyState.HOME  
						 ){answer("requesttrolleyprevstate", "state_home", "state_home(_)"   )  
						}
						if(  State == wasteservice.TransportTrolleyState.TO_INDOOR  
						 ){answer("requesttrolleyprevstate", "state_toindoor", "state_toindoor(_)"   )  
						}
						if(  State == wasteservice.TransportTrolleyState.PICKUP  
						 ){answer("requesttrolleyprevstate", "state_pickup", "state_pickup(_)"   )  
						}
						if(  State == wasteservice.TransportTrolleyState.TO_BOX  
						 ){answer("requesttrolleyprevstate", "state_tobox", "state_tobox(_)"   )  
						}
						if(  State == wasteservice.TransportTrolleyState.DUMP  
						 ){answer("requesttrolleyprevstate", "state_dump", "state_dump(_)"   )  
						}
						if(  State == wasteservice.TransportTrolleyState.TO_HOME  
						 ){answer("requesttrolleyprevstate", "state_tohome", "state_tohome(_)"   )  
						}
						if(  State == wasteservice.TransportTrolleyState.STOPPED  
						 ){answer("requesttrolleyprevstate", "state_stopped", "state_stopped(_)"   )  
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
