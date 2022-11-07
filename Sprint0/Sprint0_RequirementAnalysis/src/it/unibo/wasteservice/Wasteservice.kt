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
		
				var CurrentPlastic: Float = 0.0f
				var CurrentGlass: Float = 0.0f
				var Type: wasteservice.WasteType
				var TruckLoad: Float
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						
									var CurrentPlastic = 0.0f
									var CurrentGlass = 0.0f
						println("$name in ${currentState.stateName} | $currentMsg")
						println("[WasteService] Reset:")
						println("	Plastic: $CurrentPlastic")
						println("	Glass: $CurrentGlass")
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
					 transition(edgeName="t00",targetState="state_handle_store",cond=whenRequest("storerequest"))
					transition(edgeName="t01",targetState="state_handle_distance",cond=whenDispatch("distance"))
				}	 
				state("state_handle_store") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("storerequest(wasteType,truckLoad)"), Term.createTerm("storerequest(TYPE,TRUCKLOAD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												Type = wasteservice.WasteType.valueOf(payloadArg(0))
												TruckLoad = payloadArg(1).toFloat()
								if( 
													// enough space
													(Type == wasteservice.WasteType.PLASTIC && CurrentPlastic + TruckLoad <= wasteservice.Constants.MAXPB) ||
													(Type == wasteservice.WasteType.GLASS && CurrentGlass + TruckLoad <= wasteservice.Constants.MAXGB)
								 ){answer("storerequest", "loadaccepted", "loadaccepted(_)"   )  
								forward("doDeposit", "doDeposit(TYPE,WEIGHT)" ,"transporttrolley" ) 
								}
								else
								 {answer("storerequest", "loadrejected", "loadaccepted(_)"   )  
								 }
								forward("updategui", "updategui(_)" ,"wasteservicestatusgui" ) 
						}
						delay(1000) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("state_handle_distance") { //this:State
					action { //it:State
						if(  payloadArg(0).toFloat() >= wasteservice.Constants.DLIMT  
						 ){forward("stop", "stop(_)" ,"transporttrolley" ) 
						}
						else
						 {forward("resume", "resume(_)" ,"transporttrolley" ) 
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
