package it.unibo.wasteservice

import CoapObserver

import it.unibo.kactor.MsgUtil
import it.unibo.kactor.QakContext
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.utils.ColorsOut
import unibo.comm22.utils.CommSystemConfig
import unibo.comm22.utils.CommUtils
import wasteservice.WasteType
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.concurrent.thread
import kotlin.test.assertTrue

/**
 *
 *
 */
class TestTransportTrolley {
    companion object {
        const val hostname = "localhost"
        const val port = 11703
        const val actorName = "transporttrolley_tt_test"
        const val ctxName = "ctx_transporttrolley_test"
    }
    private lateinit var obs: CoapObserver
    private lateinit var interaction: Interaction2021

    @BeforeTest
    fun up() {
        CommSystemConfig.tracing = false

        ColorsOut.outappl("===== TEST Started =====", ColorsOut.CYAN);

        obs = CoapObserver()

        var thread = thread {
            RunCtxTransportTrolleyTest().main()
        }

        waitForActor(actorName)
        //waitForActor("wasteservice_tt_test")

        obs.addContext(ctxName, Pair(hostname, port))
        obs.addActor(actorName, ctxName)
        //obs.addActor("wasteservice_tt_test", ctxName)

        obs.createCoapConnection(actorName)
        //obs.createCoapConnection("wasteservice_tt_test")

        try {
            interaction = TcpClientSupport.connect(hostname, port, 1);
            ColorsOut.outappl("TCP Connected to the context established.", ColorsOut.MAGENTA);
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }
    }

    @AfterTest
    fun down() {
        obs.clearCoapHistory()
        obs.closeAllCoapConnections()

        try {
            interaction.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        CommUtils.delay(1000)
        ColorsOut.outappl("===== TEST Completed =====", ColorsOut.CYAN)
        CommUtils.delay(1000)
    }

    @Test
    fun testDepositGLASS() {
        ColorsOut.outappl("TEST: Check if the TransportTrolley performs the GLASS deposit correctly, according to COAP history.", ColorsOut.CYAN);

        obs.clearCoapHistory()

        ColorsOut.outappl("[WasteService] Sending deposit request to TransportTrolley.", ColorsOut.MAGENTA);
        var reply = simulateDepositRequest(interaction, WasteType.GLASS)
        ColorsOut.outappl("[WasteService] Received reply: $reply.", ColorsOut.MAGENTA);

        assertTrue(reply.contains("pickupcompleted"))
        if(reply.contains("pickupcompleted"))
            ColorsOut.outappl("TEST Deposit reply containing pickupcompleted: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST Deposit reply containing pickupcompleted: FAILED", ColorsOut.RED);

        // Wait the TransportTrolley to get home
        obs.waitForSpecificHistoryEntry("transporttrolley_tt_test(move_to_home)", timeout= 5000)

        val actionsList = mutableListOf(
            "transporttrolley_tt_test(handle_deposit_request)",
            "transporttrolley_tt_test(move_to_indoor)",
            "transporttrolley_tt_test(pickup)",
            "transporttrolley_tt_test(move_to_glassbox)",
            "transporttrolley_tt_test(dump)",
            "transporttrolley_tt_test(move_to_home)"
        )
        assertTrue(obs.checkIfHystoryContainsOrdered(actionsList, strict=true))
        if(obs.checkIfHystoryContainsOrdered(actionsList, strict=true))
            ColorsOut.outappl("TEST TransportTrolley performing the deposit action: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST TransportTrolley performing the deposit action: FAILED", ColorsOut.RED);

        obs.clearCoapHistory()
        obs.closeAllCoapConnections()
    }

    @Test
    fun testDepositPLASTIC() {
        ColorsOut.outappl("TEST: Check if the TransportTrolley performs the PLASTIC deposit correctly, according to COAP history.", ColorsOut.CYAN);

        obs.clearCoapHistory()
        ColorsOut.outappl("[WasteService] Sending deposit request to TransportTrolley.", ColorsOut.MAGENTA);
        var reply = simulateDepositRequest(interaction, WasteType.PLASTIC)
        ColorsOut.outappl("[WasteService] Received reply: $reply.", ColorsOut.MAGENTA);

        assertTrue(reply.contains("pickupcompleted"))
        if(reply.contains("pickupcompleted"))
            ColorsOut.outappl("TEST Deposit reply containing pickupcompleted: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST Deposit reply containing pickupcompleted: FAILED", ColorsOut.RED);

        obs.waitForSpecificHistoryEntry("transporttrolley_tt_test(move_to_home)", timeout=5000)

        // Check COAP history
        val actionsList = mutableListOf(
            "transporttrolley_tt_test(handle_deposit_request)",
            "transporttrolley_tt_test(move_to_indoor)",
            "transporttrolley_tt_test(pickup)",
            "transporttrolley_tt_test(move_to_plasticbox)",
            "transporttrolley_tt_test(dump)",
            "transporttrolley_tt_test(move_to_home)"
        )
        assertTrue(obs.checkIfHystoryContainsOrdered(actionsList, strict=true))
        if(obs.checkIfHystoryContainsOrdered(actionsList, strict=true))
            ColorsOut.outappl("TEST TransportTrolley performing the deposit action: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST TransportTrolley performing the deposit action: FAILED", ColorsOut.RED);

        obs.clearCoapHistory()
        obs.closeAllCoapConnections()

    }

    /*@Test
    fun testUpdateStorage() {
        ColorsOut.outappl("TEST: Check if the WasteService correctly updates the storage values, according to the COAP history.", ColorsOut.CYAN);

        var wType: WasteType = WasteType.PLASTIC
        var wWeight: Double = 10.0

        obs.clearCoapHistory()

        ColorsOut.outappl("[SmartDevice] Sending store request to WasteService.", ColorsOut.MAGENTA);
        var reply = simulateStoreRequest(interaction, wType, wWeight)
        ColorsOut.outappl("[SmartDevice] Received reply: $reply.", ColorsOut.MAGENTA);

        assertTrue(reply.contains("loadaccepted"))
        if(reply.contains("loadaccepted"))
            ColorsOut.outappl("TEST StoreRequest reply containing loadaccepted: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST StoreRequest reply containing loadaccepted: FAILED", ColorsOut.RED);

        obs.waitForSpecificHistoryEntry("wasteservice_tt_test(deposit_handled)", timeout=5000)

        // Check COAP history
        val actionsList = mutableListOf(
            "wasteservice_tt_test(state_handle_storerequest(${wType.name}: ${wWeight}/${0.0}/${WSconstants.DEFAULT_MAX}))",
            "wasteservice_tt_test(state_deposit_completed(${wType.name}: ${wWeight}/${wWeight}/${WSconstants.DEFAULT_MAX}))"
        )

        assertTrue(obs.checkIfHystoryContainsOrdered(actionsList))
        if(obs.checkIfHystoryContainsOrdered(actionsList))
            ColorsOut.outappl("TEST WasteService storage update: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST WasteService storage update: FAILED", ColorsOut.RED);

        obs.clearCoapHistory()
        obs.closeAllCoapConnections()
    }*/

    private fun simulateStoreRequest(interaction: Interaction2021, wType: WasteType, wWeight: Double): String {
        val request = MsgUtil.buildRequest("smartdevice_test", "storerequest",
            "storerequest(${wType.name}, $wWeight)", "wasteservice_tt_test").toString()
        var reply = ""

        try {
            reply = interaction.request(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return reply
    }
    private fun simulateDepositRequest(interaction: Interaction2021, wType: WasteType): String {
        val request = MsgUtil.buildRequest("wasteservice_test", "deposit",
            "deposit($wType)", "transporttrolley_tt_test").toString()
        var reply = ""

        try {
            reply = interaction.request(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return reply
    }

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
}