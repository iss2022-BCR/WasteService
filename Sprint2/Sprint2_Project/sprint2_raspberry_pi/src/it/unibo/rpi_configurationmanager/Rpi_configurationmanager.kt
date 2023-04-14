/* Generated by AN DISI Unibo */ 
package it.unibo.rpi_configurationmanager

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Rpi_configurationmanager ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "state_init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("state_init") { //this:State
					action { //it:State
						println("[RPiConfigurationManager] Started.")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="state_setup", cond=doswitch() )
				}	 
				state("state_setup") { //this:State
					action { //it:State
						 
									`it.unibo`.radarSystem22.domainBCR.utils.DomainSystemConfig.setTheConfiguration("RaspberryDomainConfig.json")
									println("[RPiConfigurationManager] Simulation: ${`it.unibo`.radarSystem22.domainBCR.utils.DomainSystemConfig.simulation}")
						forward("setup", "setup" ,"sonar_bcr" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
}