/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_wasteservice
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "192.168.137.1", this, "wasteservice_rpi.pl", "sysRules.pl","ctx_wasteservice"
	)
}
