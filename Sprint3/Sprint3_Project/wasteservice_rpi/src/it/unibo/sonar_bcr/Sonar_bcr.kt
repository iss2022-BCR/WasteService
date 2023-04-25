/* Generated by AN DISI Unibo */ 
package it.unibo.sonar_bcr

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Sonar_bcr ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[SonarBCR] Started.")
						wasteservice.raspberry.sonar.sonarBuilder.createSonar(  )
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_activate", cond=doswitch() )
				}	 
				state("state_activate") { //this:State
					action { //it:State
						forward("sonaractivate", "sonaractivate(_)" ,"sonarinput" ) 
						println("[SonarBCR] Sent activation message.")
						delay(10000) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("state_deactivate") { //this:State
					action { //it:State
						forward("sonardeactivate", "sonardeactivate(_)" ,"sonarinput" ) 
						println("[SonarBCR] Sent deactivation message.")
						delay(5000) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_activate", cond=doswitch() )
				}	 
			}
		}
}
