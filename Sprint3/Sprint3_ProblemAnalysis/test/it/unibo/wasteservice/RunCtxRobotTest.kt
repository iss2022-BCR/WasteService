package it.unibo.wasteservice

import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking

class RunCtxRobotTest {
    fun main() = runBlocking {
        QakContext.createContexts(
            "localhost", this, "sprint3_ws_test.pl", "sysRules.pl","ctx_robot"
        )
    }
}