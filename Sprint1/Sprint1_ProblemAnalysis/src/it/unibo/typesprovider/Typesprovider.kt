/* Generated by AN DISI Unibo */ 
package it.unibo.typesprovider

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Typesprovider ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[TypesProvider] Started")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("state_idle") { //this:State
					action { //it:State
						println("[TypesProvider] Waiting for type requests...")
						discardMessages = false
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t10",targetState="state_handle_wastetypes_request",cond=whenRequest("wastetypes_request"))
				}	 
				state("state_handle_wastetypes_request") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						answer("wastetypes_request", "wastetypes_reply", "wastetypes_reply(_)"   )  
						println("[TypesProvider] Replied with types list")
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
