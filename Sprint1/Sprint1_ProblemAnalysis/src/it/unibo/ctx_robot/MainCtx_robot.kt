/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_robot
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "waste_service.pl", "sysRules.pl","ctx_robot"
	)
}

