/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_wasteservice
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "demo_system_overview.pl", "sysRules.pl","ctx_wasteservice"
	)
}

