/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_echo_wasteservice
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "test_echo_waste_server.pl", "sysRules.pl","ctx_echo_wasteservice"
	)
}
