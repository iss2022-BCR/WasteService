/* Generated by AN DISI Unibo */ 
package it.unibo.ctx_basicrobot
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "demo.pl", "sysRules.pl","ctx_basicrobot"
	)
}

