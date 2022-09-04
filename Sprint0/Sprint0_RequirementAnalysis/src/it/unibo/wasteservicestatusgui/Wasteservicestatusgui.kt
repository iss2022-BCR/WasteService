/* Generated by AN DISI Unibo */ 
package it.unibo.wasteservicestatusgui

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Wasteservicestatusgui ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var trolleyState: wasteservice.TrolleyState
				var trolleyPosition: wasteservice.TrolleyPosition
				var currentPlastic: Float
				var currentGlass: Float
				var ledState: wasteservice.LedState
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[WasteServiceStatusGUI] Initializing...")
						
									trolleyState = wasteservice.TrolleyState.IDLE
									trolleyPosition = wasteservice.TrolleyPosition.HOME
									currentPlastic = 0.0f
									currentGlass = 0.0f
									ledState = wasteservice.LedState.OFF
						println("[WasteServiceStatusGUI] Initialization completed. Current state:
						
												TrolleyState: $trolleyState
						
												TrolleyPosition: $trolleyPosition
						
												Plastic: $currentPlastic KG
						
												Glass: $currentGlass KG
						
												LedState: $ledState")
					}
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("state_idle") { //this:State
					action { //it:State
						println("[WasteServiceStatusGUI] Waiting for updates...")
					}
					 transition(edgeName="t09",targetState="state_handle_update",cond=whenDispatch("updategui"))
				}	 
				state("state_handle_update") { //this:State
					action { //it:State
						println("[WasteServiceStatusGUI] State:
												TrolleyState: $trolleyState
						
												TrolleyPosition: $trolleyPosition
						
												Plastic: $currentPlastic KG
						
												Glass: $currentGlass KG
						
												LedState: $ledState")
					}
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
			}
		}
}