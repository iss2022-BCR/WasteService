package it.unibo.wasteservice

import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking

class RunWasteService {
    fun main() = runBlocking {
        QakContext.createContexts(
            "localhost", this, "test_waste_service.pl", "sysRules.pl","ctx_wasteservice_test"
        )
    }
}