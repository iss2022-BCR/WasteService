/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_transporttrolley
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "sprint3_wasteservice_core.pl", "sysRules.pl","ctx_transporttrolley"
	)
}

