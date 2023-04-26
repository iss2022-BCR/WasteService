package it.unibo.wasteservice

import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking

class RunCtxStatusGuiTest {
    fun main() = runBlocking {
        QakContext.createContexts(
            "localhost", this, "sprint3_ws_test.pl", "sysRules.pl","ctx_statusgui"
        )
    }
}