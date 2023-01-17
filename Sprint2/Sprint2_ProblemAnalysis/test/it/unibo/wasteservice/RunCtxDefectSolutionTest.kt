package it.unibo.wasteservice

import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking

/**
 * Utility class that runs the context for the DefectSolution test: ctx_typesprovider_test.
 */
class RunCtxDefectSolutionTest {
    fun main() = runBlocking {
        QakContext.createContexts(
            "localhost", this, "test_defect_solution.pl", "sysRules.pl","ctx_defect_solution_test"
        )
    }
}