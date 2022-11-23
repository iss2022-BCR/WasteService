package it.unibo.wasteservice

import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking

/**
 * Utility class that runs the context for the TransportTrolley test: ctx_transporttrolley_test.
 */
class RunCtxTransportTrolleyTest {
    fun main() = runBlocking {
        QakContext.createContexts(
            "localhost", this, "test_transport_trolley.pl", "sysRules.pl","ctx_transporttrolley_test"
        )
    }
}