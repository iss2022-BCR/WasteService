/* Generated by AN DISI Unibo */ 
package it.unibo.led_test

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Led_test ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[LedTest] Started.")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_off", cond=doswitch() )
				}	 
				state("state_off") { //this:State
					action { //it:State
						 wasteservice.raspberry.led.ledSupportBCR.doLed(wasteservice.LedState.OFF)  
						 wasteservice.raspberry.display.displaySupportBCR.writeToDisplay("", "Trolley at HOME")  
						println("[LedTest] Led OFF.")
						delay(3000) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_on", cond=doswitch() )
				}	 
				state("state_on") { //this:State
					action { //it:State
						 wasteservice.raspberry.led.ledSupportBCR.doLed(wasteservice.LedState.ON)  
						 wasteservice.raspberry.display.displaySupportBCR.writeToDisplay("", "Trolley STOPPED")  
						println("[LedTest] Led ON.")
						delay(3000) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_blink", cond=doswitch() )
				}	 
				state("state_blink") { //this:State
					action { //it:State
						 wasteservice.raspberry.led.ledSupportBCR.doLed(wasteservice.LedState.BLINKING)  
						 wasteservice.raspberry.display.displaySupportBCR.writeToDisplay("", "Trolley MOVING")  
						println("[LedTest] Led BLINKING.")
						delay(5000) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_off", cond=doswitch() )
				}	 
			}
		}
}
