package it.unibo.wasteservice

import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking

/**
 * Utility class that runs the context for the WasteService test: ctx_wasteservice_test.
 */
class RunCtxWasteServiceTest {
    fun main() = runBlocking {
        QakContext.createContexts(
            "localhost", this, "test_waste_service.pl", "sysRules.pl","ctx_wasteservice_test"
        )
    }
}