/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_wasteservice_test
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "test_waste_service.pl", "sysRules.pl","ctx_wasteservice_test"
	)
}

