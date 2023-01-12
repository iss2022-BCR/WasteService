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
				
				var WaitingPickup: Boolean = false
				var WaitingDeposit: Boolean = false
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[WasteService] Started.")
						println("[WasteService] Current storage:")
						 WasteService.printFancyStatusString()  
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
					 transition(edgeName="t11",targetState="state_handle_storerequest",cond=whenRequest("storerequest"))
				}	 
				state("state_handle_storerequest") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("storerequest(TYPE,LOAD)"), Term.createTerm("storerequest(TYPE,LOAD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var Type = payloadArg(0)
												var Load = payloadArg(1)
								println("[WasteService] Received storerequest($Type, $Load)")
								if(  wasteservice.Utils.isValidWasteType(Type) &&
													wasteservice.Utils.isValidWasteWeight(Load)  
								 ){
													RequestedWasteType = wasteservice.WasteType.valueOf(Type)
													RequestedWasteWeight = payloadArg(1).toDouble()
								if(  WasteService.canPreStore(RequestedWasteType, RequestedWasteWeight)  
								 ){
														WasteService.addToPreStorage(RequestedWasteType, RequestedWasteWeight)
													
														WaitingPickup = true
								println("[WasteService] There is enough space.")
								request("deposit", "deposit($RequestedWasteType,$RequestedWasteWeight)" ,"transporttrolley" )  
								}
								else
								 { WaitingPickup = false  
								  wasteservice.Utils.printFail("[WasteService] Load rejected: there is not enough space in the container to store the load.")  
								 answer("storerequest", "loadrejected", "loadrejected(_)"   )  
								 }
								}
								else
								 { WaitingPickup = false  
								  wasteservice.Utils.printFail("[WasteService] Load rejected: one or more parameters are invalid.")  
								 answer("storerequest", "loadrejected", "loadrejected(_)"   )  
								 }
								println("[WasteService] Current storage:")
								 WasteService.printFancyStatusString()  
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
					 transition(edgeName="t12",targetState="state_pickup_completed",cond=whenReply("pickupcompleted"))
				}	 
				state("state_pickup_completed") { //this:State
					action { //it:State
						
									WaitingPickup = false
									WaitingDeposit = true
						 wasteservice.Utils.printCorrect("[WasteService] Pickup completed, sent Load Accepted.")  
						answer("storerequest", "loadaccepted", "loadaccepted(_)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_waiting_deposit", cond=doswitch() )
				}	 
				state("state_waiting_deposit") { //this:State
					action { //it:State
						println("[WasteService] Waiting for deposit completion...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t13",targetState="state_deposit_completed",cond=whenDispatch("depositcompleted"))
					transition(edgeName="t14",targetState="state_deposit_failed",cond=whenDispatch("depositfailed"))
				}	 
				state("state_deposit_completed") { //this:State
					action { //it:State
						
									WasteService.addToStorage(RequestedWasteType, RequestedWasteWeight)
									
									WaitingDeposit = false
						println("[WasteService] Deposit completed. Current storage:")
						 WasteService.printFancyStatusString()  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("state_deposit_failed") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("depositfailed(REASON)"), Term.createTerm("depositfailed(REASON)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												// Reset PreStorage
												
												WaitingDeposit = false
												var Error = payloadArg(0)
								println("[WasteService] Deposit failed: $Error.")
						}
						println("[WasteService] Current storage:")
						 WasteService.printFancyStatusString()  
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
