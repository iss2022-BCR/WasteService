/* Generated by AN DISI Unibo */ 
package it.unibo.ledcontroller_ls_test

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Ledcontroller_ls_test ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 var State: wasteservice.LedState = wasteservice.LedState.OFF  
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[LedController] Started.")
						updateResourceRep( "ledstate(OFF)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("state_idle") { //this:State
					action { //it:State
						println("[LedController] Led state: ${State.name}")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t018",targetState="state_handle",cond=whenEvent("trolley_state_changed"))
				}	 
				state("state_handle") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("trolley_state_changed(STATE)"), Term.createTerm("trolley_state_changed(STATE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var NewState = wasteservice.Utils.getLedStateFromTrolleyState(payloadArg(0))  
								if(  NewState != State  
								 ){println("[LedController] Led state changed!")
								 State = NewState  
								updateResourceRep( "ledstate($NewState)"  
								)
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
