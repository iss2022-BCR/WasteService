package it.unibo.wasteservice

import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking

/**
 * Utility class that runs the context for the TypesProvider test: ctx_typesprovider_test.
 */
class RunCtxTypesProviderTest {
    fun main() = runBlocking {
        QakContext.createContexts(
        "localhost", this, "test_types_provider.pl", "sysRules.pl","ctx_typesprovider_test"
        )
    }
}