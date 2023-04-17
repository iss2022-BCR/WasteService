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
		
				var WasteTypes: String = wasteservice.Utils.getWasteTypesString("_")
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[TypesProvider] Started. WasteTypes: $WasteTypes")
						discardMessages = false
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("state_idle") { //this:State
					action { //it:State
						println("[TypesProvider] Waiting for type requests...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="state_handle_wastetypes_request",cond=whenRequest("typesrequest"))
				}	 
				state("state_handle_wastetypes_request") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						answer("typesrequest", "typesreply", "typesreply($WasteTypes)"   )  
						println("[TypesProvider] Replied with types list: $WasteTypes")
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