/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_raspberrypi
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "sprint2_raspberry_pi.pl", "sysRules.pl","ctx_raspberrypi"
	)
}
