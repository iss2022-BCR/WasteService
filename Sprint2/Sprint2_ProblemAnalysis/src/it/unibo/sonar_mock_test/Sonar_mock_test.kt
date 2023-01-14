/* Generated by AN DISI Unibo */ 
package it.unibo.sonar_mock_test

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Sonar_mock_test ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[SonarMockTest] Started.")
						delay(10000) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_detect", cond=doswitch() )
				}	 
				state("state_detect") { //this:State
					action { //it:State
						 var Distance = kotlin.random.Random.nextInt(0, 100)  
						println("[SonarMockTest] Sending distance: $Distance")
						forward("sonar_data", "sonar_data($Distance)" ,"alarm_controller" ) 
						delay(7000) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_detect", cond=doswitch() )
				}	 
			}
		}
}
