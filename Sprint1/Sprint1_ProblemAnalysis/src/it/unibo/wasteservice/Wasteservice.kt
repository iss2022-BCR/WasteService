/* Generated by AN DISI Unibo */ 
package it.unibo.wasteservice

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Wasteservice ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var RequestedWasteType: wasteservice.WasteType = wasteservice.WasteType.values()[0]
				var RequestedWasteWeight: Double = 1.0
				
				val WasteService = wasteservice.WasteService()
				var StorageStats: String = WasteService.getFullStatusString()
				
				var WaitingPickup: Boolean = false
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[WasteService] Started.")
						println("[WasteService] Current storage:")
						println("$StorageStats")
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
						println("[WasteService] Waiting for store requests...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t14",targetState="state_handle_storerequest",cond=whenRequest("storerequest"))
				}	 
				state("state_handle_storerequest") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("storerequest(TYPE,LOAD)"), Term.createTerm("storerequest(TYPE,LOAD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("[WasteService] Received storerequest($RequestedWasteType, $RequestedWasteWeight)")
								if(  wasteservice.Utils.isValidWasteType(payloadArg(0)) &&
													wasteservice.Utils.isValidWasteWeight(payloadArg(1))  
								 ){
													RequestedWasteType = wasteservice.WasteType.valueOf(payloadArg(0))
													RequestedWasteWeight = payloadArg(1).toDouble()
								if(  WasteService.canPreStore(RequestedWasteType, RequestedWasteWeight)  
								 ){
														WasteService.addToPreStorage(RequestedWasteType, RequestedWasteWeight)
														StorageStats = WasteService.getFullStatusString()
													
														WaitingPickup = true
								println("[WasteService] There is enough space.")
								request("deposit", "deposit($RequestedWasteType)" ,"transporttrolley" )  
								}
								else
								 { WaitingPickup = false  
								 println("[WasteService] Load rejected: there is not enough space in the container to store the load.")
								 answer("storerequest", "loadrejected", "loadrejected(_)"   )  
								 }
								}
								else
								 { WaitingPickup = false  
								 println("[WasteService] Load rejected: one or more parameters are invalid.")
								 answer("storerequest", "loadrejected", "loadrejected(_)"   )  
								 }
								println("[WasteService] Current storage:")
								println("$StorageStats")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitchGuarded({ WaitingPickup == false  
					}) )
					transition( edgeName="goto",targetState="state_waiting_pickup", cond=doswitchGuarded({! ( WaitingPickup == false  
					) }) )
				}	 
				state("state_waiting_pickup") { //this:State
					action { //it:State
						println("[WasteService] Waiting for pickup completion...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t15",targetState="state_pickup_completed",cond=whenReply("pickupcompleted"))
				}	 
				state("state_pickup_completed") { //this:State
					action { //it:State
						
									WasteService.addToStorage(RequestedWasteType, RequestedWasteWeight)
									StorageStats = WasteService.getFullStatusString()
									
									WaitingPickup = true
						println("[WasteService] Pickup completed, sent Load accepted.")
						answer("storerequest", "loadaccepted", "loadaccepted(_)"   )  
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
