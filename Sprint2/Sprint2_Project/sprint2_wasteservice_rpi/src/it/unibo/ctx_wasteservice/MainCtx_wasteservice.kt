/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_wasteservice
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "192.168.1.4", this, "sprint2_wasteservice_rpi.pl", "sysRules.pl","ctx_wasteservice"
	)
}

