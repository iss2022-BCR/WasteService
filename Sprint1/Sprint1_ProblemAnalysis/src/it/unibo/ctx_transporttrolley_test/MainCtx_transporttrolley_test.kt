/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_transporttrolley_test
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "test_transport_trolley.pl", "sysRules.pl","ctx_transporttrolley_test"
	)
}
