/* Generated by AN DISI Unibo */ 
package it.unibo.smartdevice_simulator

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Smartdevice_simulator ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var Types: String = ""
				var TypesList = arrayListOf<wasteservice.WasteType>()
				
				var WasteType: wasteservice.WasteType = wasteservice.WasteType.PLASTIC
				var WasteWeight: Double = 10.0
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[SmartDevice Simulator] Started")
						discardMessages = false
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_typesrequest", cond=doswitch() )
				}	 
				state("state_typesrequest") { //this:State
					action { //it:State
						delay(1000) 
						request("typesrequest", "typesrequest(_)" ,"typesprovider" )  
						println("[SmartDevice Simulator] Sent typesrequest()")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t10",targetState="state_handle_types",cond=whenReply("typesreply"))
				}	 
				state("state_handle_types") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("typesreply(TYPES)"), Term.createTerm("typesreply(TYPES)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												Types = payloadArg(0).toString()
												TypesList = wasteservice.Utils.getWasteTypesList(Types, "_")
						}
						println("[SmartDevice Simulator] Types list: $TypesList")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_storerequest", cond=doswitch() )
				}	 
				state("state_storerequest") { //this:State
					action { //it:State
						
									WasteType = wasteservice.WasteType.values()[kotlin.random.Random.nextInt(0, TypesList.size)]
						delay(1000) 
						request("storerequest", "storerequest($WasteType,$WasteWeight)" ,"wasteservice" )  
						println("[SmartDevice Simulator] Sent storerequest($WasteType, $WasteWeight)")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t21",targetState="state_handle_loadaccepted",cond=whenReply("loadaccepted"))
					transition(edgeName="t22",targetState="state_handle_loadrejected",cond=whenReply("loadrejected"))
				}	 
				state("state_handle_loadaccepted") { //this:State
					action { //it:State
						println("[SmartDevice Simulator] Received LoadAccepted")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_storerequest", cond=doswitch() )
				}	 
				state("state_handle_loadrejected") { //this:State
					action { //it:State
						println("[SmartDevice Simulator] Received LoadRejected")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_storerequest", cond=doswitch() )
				}	 
			}
		}
}
