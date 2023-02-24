/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolley_ls_test

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Transporttrolley_ls_test ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 
				var Actions: String = "" 
				var Progress = ""
				
				var WasteType: wasteservice.WasteType = wasteservice.WasteType.PLASTIC
				var WasteLoad: Double = 0.0
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						
									plannerBCR.initAI()
									plannerBCR.loadRoomMap("mapRoomEmpty")
									plannerBCR.showMap()
									plannerBCR.loadMapConfig("mapConfigWasteService")
									println("MapConfig:")
									plannerBCR.showFancyMapConfig()
						println("[TransportTrolley] Started.")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_idle", cond=doswitch() )
				}	 
				state("state_idle") { //this:State
					action { //it:State
						updateResourceRep( "transporttrolley(HOME)"  
						)
						println("[TransportTrolley] Waiting for deposit requests...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="state_handle_deposit_request",cond=whenRequest("deposit"))
				}	 
				state("state_handle_deposit_request") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("deposit(TYPE,LOAD)"), Term.createTerm("deposit(TYPE,LOAD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												WasteType = wasteservice.WasteType.valueOf(payloadArg(0))
												WasteLoad = payloadArg(1).toDouble()
								println("[TransportTrolley] Deposit request received.")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_move_to_indoor", cond=doswitch() )
				}	 
				state("state_move_to_indoor") { //this:State
					action { //it:State
						updateResourceRep( "transporttrolley(MOVING)"  
						)
						println("[TransportTrolley] Moving to INDOOR...")
						
									val curPos: Pair<Int, Int> = plannerBCR.get_curPos()
									val goal: Pair<Int, Int> = plannerBCR.getNearestPositionToCellType(curPos, "INDOOR")
									plannerBCR.setGoal(goal.first, goal.second)
									plannerBCR.doPlan()
									Actions = plannerBCR.getActionsString()
						request("dopath", "dopath($Actions,transporttrolley)" ,"pathexecutorbcr_ls_test" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t11",targetState="state_pickup",cond=whenReply("dopathdone"))
					transition(edgeName="t12",targetState="state_error",cond=whenReply("dopathfail"))
				}	 
				state("state_pickup") { //this:State
					action { //it:State
						updateResourceRep( "transporttrolley(PICKUP)"  
						)
						println("[TransportTrolley] Picking up the load of $WasteType...")
						 wasteservice.Utils.simulateAction(WasteLoad)  
						println("[TransportTrolley] Pickup completed.")
						answer("deposit", "pickupcompleted", "pickupcompleted(_)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_move_to_box", cond=doswitch() )
				}	 
				state("state_move_to_box") { //this:State
					action { //it:State
						updateResourceRep( "transporttrolley(MOVING)"  
						)
						println("[TransportTrolley] Moving to ${WasteType.name}_BOX...")
						
									val curPos: Pair<Int, Int> = plannerBCR.get_curPos()
									val goal: Pair<Int, Int> = plannerBCR.getNearestPositionToCellType(curPos, WasteType.name)
									plannerBCR.setGoal(goal.first, goal.second)
									plannerBCR.doPlan()
									Actions = plannerBCR.getActionsString()
						request("dopath", "dopath($Actions,transporttrolley)" ,"pathexecutorbcr_ls_test" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t23",targetState="state_dump",cond=whenReply("dopathdone"))
					transition(edgeName="t24",targetState="state_error",cond=whenReply("dopathfail"))
				}	 
				state("state_dump") { //this:State
					action { //it:State
						updateResourceRep( "transporttrolley(DUMP)"  
						)
						println("[TransportTrolley] Dumping the load...")
						 wasteservice.Utils.simulateAction(WasteLoad)  
						println("[TransportTrolley] Dump completed.")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_move_to_home", cond=doswitch() )
				}	 
				state("state_move_to_home") { //this:State
					action { //it:State
						updateResourceRep( "transporttrolley(MOVING)"  
						)
						println("[TransportTrolley] Moving to HOME...")
						
									val curPos: Pair<Int, Int> = plannerBCR.get_curPos()
									val goal: Pair<Int, Int> = plannerBCR.getNearestPositionToCellType(curPos, "HOME")
									plannerBCR.setGoal(goal.first, goal.second)
									plannerBCR.doPlan()
									Actions = plannerBCR.getActionsString()
						request("dopath", "dopath($Actions,transporttrolley)" ,"pathexecutorbcr_ls_test" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t35",targetState="state_idle",cond=whenReply("dopathdone"))
					transition(edgeName="t36",targetState="state_error",cond=whenReply("dopathfail"))
				}	 
				state("state_error") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("dopathfail(ARG)"), Term.createTerm("dopathfail(PATH_STILL_TO_DO)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var PathStillToDo = payloadArg(0)
								println("[TransportTrolley] An Error occurred while trying to move along a path.")
								println("[TransportTrolley] Path still to do: $PathStillToDo")
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
