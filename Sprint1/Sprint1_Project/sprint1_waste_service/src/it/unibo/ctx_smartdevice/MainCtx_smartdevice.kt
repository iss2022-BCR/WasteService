/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_smartdevice
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "logical_architecture_waste_service.pl", "sysRules.pl","ctx_smartdevice"
	)
}
