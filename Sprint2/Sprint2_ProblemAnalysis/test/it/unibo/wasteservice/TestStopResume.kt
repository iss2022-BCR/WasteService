package it.unibo.wasteservice

import CoapObserver
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.QakContext
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.utils.ColorsOut
import unibo.comm22.utils.CommSystemConfig
import unibo.comm22.utils.CommUtils
import wasteservice.WSconstants
import wasteservice.WasteType
import kotlin.concurrent.thread
import kotlin.test.assertTrue
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Stop/Resume Test: alarm situations handling.
 *
 * Test Recap:
 *  1) TEST: TransportTrolley is at HOME
 *  2) Mock_Sonar sends a distance < DLIM to the AlarmController
 *  3) TEST: TransportTrolley enters STOPPED state
 *  4) Mock_Sonar sends a distance > DLIM to the AlarmController
 *  5) TEST: TransportTrolley is at HOME state (resumed)
 *
 *  6) Mock_WasteService sends a deposit request to TransportTrolley
 *  7) TEST: TransportTrolley starts MOVING
 *  8) Mock_Sonar sends a distance < DLIM to the AlarmController
 *  9) TEST: TransportTrolley enters STOPPED state
 *
 *  10) Mock_Sonar sends a distance > DLIM to the AlarmController
 *  11) TEST: TransportTrolley is back MOVING (resumed)
 *  12) Mock_Sonar sends a distance > DLIM to the AlarmController
 *  13) TEST: TransportTrolley is still MOVING (nothing happens)
 */
class TestStopResume {
    companion object {
        private const val suffix = "_sr_test"

        private const val hostCtx = "localhost"
        private const val portCtx = 11704
        private const val ctxName = "ctx_stopresume_test"
        private const val actorNameTT = "transporttrolley$suffix"
        private const val actorNamePE = "pathexecutorbcr$suffix"
        private const val actorNameAC = "alarmcontroller$suffix"

        private const val actorNameWS = "mock_wasteservice$suffix"
        private const val actorNameSR = "mock_sonar$suffix"
    }

    private lateinit var obs: CoapObserver
    private lateinit var ctxConnection: Interaction2021

    private fun waitForActor(actorName: String) {
        var actor = QakContext.getActor(actorName)
        while(actor == null) {
            ColorsOut.outappl("Waiting for actor $actorName...", ColorsOut.MAGENTA);
            CommUtils.delay(500)
            actor = QakContext.getActor(actorName)
            println(actor)
        }
        ColorsOut.out("Actor $actorName ready.", ColorsOut.MAGENTA)
    }

    @BeforeTest
    fun startup() {
        CommSystemConfig.tracing = false

        thread {
            it.unibo.ctx_stopresume_test.main()
        }

        // wait for actors to be ready before instantiating the COAP connection (it could fail quietly!)
        waitForActor(actorNameTT)
        waitForActor(actorNamePE)
        waitForActor(actorNameAC)

        // Setup COAP
        obs = CoapObserver()
        obs.addContext(ctxName, Pair(hostCtx, portCtx))
        obs.addActor(actorNameTT, ctxName)
        obs.addActor(actorNamePE, ctxName)
        obs.createCoapConnection(actorNameTT)
        obs.createCoapConnection(actorNamePE)
        //obs.clearCoapHistory() // clearing the coap history before the test makes it fail for unknown reason

        CommUtils.delay(1000)

        // establish TCP connections
        try {
            ctxConnection = TcpClientSupport.connect(hostCtx, portCtx, 1)
            ColorsOut.outappl("TCP communication with Stop/Resume Test Context established.", ColorsOut.MAGENTA);
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }
        CommUtils.delay(1000)

        ColorsOut.outappl("===== TEST Started =====", ColorsOut.CYAN);

    }

    @AfterTest
    fun shutdown() {
        try {
            ctxConnection.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        CommUtils.delay(1000)
        ColorsOut.outappl("===== TEST Completed =====", ColorsOut.CYAN)
        CommUtils.delay(1000)

        obs.clearCoapHistory()
        obs.closeAllCoapConnections()
    }

    @Test
    fun test_stop_resume() {
        ColorsOut.outappl("TEST: Check if the TransportTrolley correctly stops while at home.", ColorsOut.CYAN);

        var res = obs.waitForSpecificHistoryEntry("transporttrolley(HOME)", timeout = 5000)
        assertTrue(res)
        if(res)
            ColorsOut.outappl("TEST START - TransportTrolley at home: OK", ColorsOut.GREEN)
        else {
            ColorsOut.outappl("TEST START - TransportTrolley at home: FAILED", ColorsOut.RED)
        }

        CommUtils.delay(1000)

        simulate_distance(wasteservice.WSconstants.DLIM - 1.0)

        res = obs.waitForSpecificHistoryEntry("transporttrolley(STOPPED)", timeout = 5000)
        assertTrue(res)
        if(res)
            ColorsOut.outappl("TEST STOP - TransportTrolley stopped: OK", ColorsOut.GREEN)
        else {
            ColorsOut.outappl("TEST STOP - TransportTrolley stopped: FAILED", ColorsOut.RED)
        }

        CommUtils.delay(1000)

        simulate_distance(WSconstants.DLIM)

        res = obs.waitForSpecificHistoryEntry("transporttrolley(HOME)", timeout = 5000)
        assertTrue(res)
        if(res)
            ColorsOut.outappl("TEST RESUME - TransportTrolley at home: OK", ColorsOut.GREEN)
        else {
            ColorsOut.outappl("TEST RESUME - TransportTrolley at home: FAILED", ColorsOut.RED)
        }

        CommUtils.delay(1000)

        simulate_deposit(WasteType.PLASTIC, 10.0)

        res = obs.waitForSpecificHistoryEntry("transporttrolley(MOVING)", timeout = 5000)
        assertTrue(res)
        if(res)
            ColorsOut.outappl("TEST DEPOSIT - TransportTrolley moving: OK", ColorsOut.GREEN)
        else {
            ColorsOut.outappl("TEST DEPOSIT - TransportTrolley moving: FAILED", ColorsOut.RED)
        }

        CommUtils.delay(1000)

        simulate_distance(WSconstants.DLIM - 5.0)

        res = obs.waitForSpecificHistoryEntry("transporttrolley(STOPPED)", timeout = 5000)
        assertTrue(res)
        if(res)
            ColorsOut.outappl("TEST STOP - TransportTrolley stopped: OK", ColorsOut.GREEN)
        else {
            ColorsOut.outappl("TEST STOP - TransportTrolley stopped: FAILED", ColorsOut.RED)
        }

        CommUtils.delay(1000)

        simulate_distance(WSconstants.DLIM + 10.0)

        res = obs.waitForSpecificHistoryEntry("transporttrolley(MOVING)", timeout = 5000)
        assertTrue(res)
        if(res)
            ColorsOut.outappl("TEST RESUME - TransportTrolley moving: OK", ColorsOut.GREEN)
        else {
            ColorsOut.outappl("TEST RESUME - TransportTrolley moving: FAILED", ColorsOut.RED)
        }

        simulate_distance(WSconstants.DLIM + 30.0)

        obs.checkLast("transporttrolley(MOVING)")

        CommUtils.delay(1000)
    }

    private fun simulate_deposit(wasteType: WasteType, wasteLoad: Double) {
        ColorsOut.outappl("[Mock_WasteService] Sending deposit($wasteType, $wasteLoad) request to TransportTrolley.", ColorsOut.MAGENTA);
        val request = MsgUtil.buildRequest(
            actorNameWS, "deposit",
            "deposit(${wasteType.name}, $wasteLoad)", actorNameTT)

        try {
            ctxConnection.forward(request.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun simulate_distance(value: Double) {
        ColorsOut.outappl("[Mock_Sonar] Sending sonar_data($value) to AlarmController.", ColorsOut.MAGENTA);

        val event = MsgUtil.buildDispatch(actorNameSR, "sonar_data", "sonar_data($value)", actorNameAC);

        try {
            ctxConnection.forward(event.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}