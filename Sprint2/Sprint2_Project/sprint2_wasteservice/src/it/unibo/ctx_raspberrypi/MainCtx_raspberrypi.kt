/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_raspberrypi
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "192.168.1.5", this, "sprint2_waste_service.pl", "sysRules.pl","ctx_raspberrypi"
	)
}

