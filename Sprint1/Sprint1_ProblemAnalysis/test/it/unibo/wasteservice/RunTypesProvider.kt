package it.unibo.wasteservice

import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking

class RunTypesProvider {
    fun main() = runBlocking {
        QakContext.createContexts(
        "localhost", this, "test_types_provider.pl", "sysRules.pl","ctx_typesprovider_test"
        )
    }
}