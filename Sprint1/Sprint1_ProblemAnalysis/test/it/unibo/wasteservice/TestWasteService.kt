package it.unibo.wasteservice

import it.unibo.kactor.ActorBasic
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

class TestWasteService {
    companion object {
        private const val hostname = "localhost"
        private const val port = 11702
        private const val actorNameWS = "wasteservice_test"
        private const val actorNameTT = "transporttrolley_test"
    }

    private var actorWasteService: ActorBasic? = null
    private var actorTransportTrolley: ActorBasic? = null
    private lateinit var interaction: Interaction2021

    private fun setup() {
        actorWasteService = QakContext.getActor(actorNameWS)
        while(actorWasteService == null) {
            CommUtils.delay(500)
            actorWasteService = QakContext.getActor(actorNameWS)
            println(actorWasteService)
        }
        ColorsOut.outappl("[WasteService] Actor correctly set up", ColorsOut.MAGENTA);

        actorTransportTrolley = QakContext.getActor(actorNameTT)
        while(actorTransportTrolley == null) {
            CommUtils.delay(500)
            actorTransportTrolley = QakContext.getActor(actorNameTT)
            println(actorTransportTrolley)
        }
        ColorsOut.outappl("[TransportTrolley] Actor correctly set up", ColorsOut.MAGENTA);

    }

    @BeforeTest
    fun startup() {
        CommSystemConfig.tracing = false

        ColorsOut.outappl("===== TEST TypesProvider =====", ColorsOut.CYAN);

        var thread = thread {
            RunWasteService().main()
        }
        setup();

        // establish TCP connection (SmartDevice Test)
        try {
            interaction = TcpClientSupport.connect(hostname, port, 1);
            ColorsOut.outappl("[SmartDevice] TCP communication established.", ColorsOut.MAGENTA);
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }
        CommUtils.delay(1000)
    }

    @AfterTest
    fun shutdown() {
        CommUtils.delay(1000)
        ColorsOut.outappl("===== TEST Completed =====", ColorsOut.CYAN)
    }

    @Test
    fun testLoadAccepted() {
        var wasteType = WasteType.PLASTIC;
        var wasteWeight = 10.0

        ColorsOut.outappl("TEST: Receive a loadaccepted reply when there is enough space to store the load.", ColorsOut.CYAN);
        val req: String = MsgUtil.buildRequest("smartdevice_test", "storerequest", "storerequest(${wasteType.name}, ${wasteWeight})", actorNameWS).toString()
        var reply: String = ""

        try {
            ColorsOut.outappl("[SmartDevice] Sending storerequest to ${actorNameWS}.", ColorsOut.MAGENTA);
            reply = interaction.request(req)
            ColorsOut.outappl("[SmartDevice] Received reply: $reply.", ColorsOut.MAGENTA);
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Check if the reply contains "loadaccepted"
        assertTrue(reply.contains("loadaccepted"))
        if(reply.contains("loadaccepted"))
            ColorsOut.outappl("TEST TypesReply containing loadaccepted: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST TypesReply containing loadaccepted: FAILED", ColorsOut.RED);
    }

    @Test
    fun testStorageUpdate() {
        var wasteType = WasteType.PLASTIC;
        var wasteWeight = WSconstants.DEFAULT_MAX;

        ColorsOut.outappl("TEST: Verify that the storage gets updated after a successful request.", ColorsOut.CYAN);
        val req: String = MsgUtil.buildRequest("smartdevice_test", "storerequest", "storerequest(${wasteType.name}, ${wasteWeight})", actorNameWS).toString()
        var reply: String = ""

        try {
            ColorsOut.outappl("[SmartDevice] Sending storerequest to ${actorNameWS}.", ColorsOut.MAGENTA);
            reply = interaction.request(req)
            ColorsOut.outappl("[SmartDevice] Received reply: $reply.", ColorsOut.MAGENTA);
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Check if the reply contains "loadaccepted"
        assertTrue(reply.contains("loadaccepted"))
        if(reply.contains("loadaccepted"))
            ColorsOut.outappl("TEST TypesReply containing loadaccepted: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST TypesReply containing loadaccepted: FAILED", ColorsOut.RED);

        reply = ""
        try {
            reply = interaction.request(req)
            ColorsOut.outappl("[SmartDevice] Received reply: $reply.", ColorsOut.MAGENTA);
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Check if the reply contains "loadrejected"
        assertTrue(reply.contains("loadrejected"))
        if(reply.contains("loadrejected"))
            ColorsOut.outappl("TEST TypesReply containing loadrejected: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST TypesReply containing loadrejected: FAILED", ColorsOut.RED);
    }

    @Test
    fun testLoadRejected1() {
        var wasteType = "ORGANIC";
        var wasteWeight = 10.0

        ColorsOut.outappl("TEST: Receive a loadrejected reply when the WasteType is invalid.", ColorsOut.CYAN);
        val req: String = MsgUtil.buildRequest("smartdevice_test", "storerequest", "storerequest(${wasteType}, ${wasteWeight})", actorNameWS).toString()
        var reply: String = ""

        try {
            ColorsOut.outappl("[SmartDevice] Sending storerequest to ${actorNameWS}.", ColorsOut.MAGENTA);
            reply = interaction.request(req)
            ColorsOut.outappl("[SmartDevice] Received reply: $reply.", ColorsOut.MAGENTA);
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Check if the reply contains "loadrejected"
        assertTrue(reply.contains("loadrejected"))
        if(reply.contains("loadrejected"))
            ColorsOut.outappl("TEST TypesReply containing loadrejected: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST TypesReply containing loadrejected: FAILED", ColorsOut.RED);
    }

    @Test
    fun testLoadRejected2() {
        var wasteType = WasteType.PLASTIC;
        var wasteWeight = 100000.0

        ColorsOut.outappl("TEST: Receive a loadrejected reply when the WasteWeight is invalid.", ColorsOut.CYAN);
        val req: String = MsgUtil.buildRequest("smartdevice_test", "storerequest", "storerequest(${wasteType.name}, ${wasteWeight})", actorNameWS).toString()
        var reply: String = ""

        try {
            ColorsOut.outappl("[SmartDevice] Sending storerequest to ${actorNameWS}.", ColorsOut.MAGENTA);
            reply = interaction.request(req)
            ColorsOut.outappl("[SmartDevice] Received reply: $reply.", ColorsOut.MAGENTA);
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Check if the reply contains "loadrejected"
        assertTrue(reply.contains("loadrejected"))
        if(reply.contains("loadrejected"))
            ColorsOut.outappl("TEST TypesReply containing loadrejected: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST TypesReply containing loadrejected: FAILED", ColorsOut.RED);
    }

    @Test
    fun testLoadRejected3() {
        var wasteType = WasteType.PLASTIC;
        var wasteWeight = WSconstants.DEFAULT_MAX + 10.0;

        ColorsOut.outappl("TEST: Receive a loadrejected reply when there is not enough space to store the load.", ColorsOut.CYAN);
        val req: String = MsgUtil.buildRequest("smartdevice_test", "storerequest", "storerequest(${wasteType.name}, ${wasteWeight})", actorNameWS).toString()
        var reply: String = ""

        try {
            ColorsOut.outappl("[SmartDevice] Sending storerequest to ${actorNameWS}.", ColorsOut.MAGENTA);
            reply = interaction.request(req)
            ColorsOut.outappl("[SmartDevice] Received reply: $reply.", ColorsOut.MAGENTA);
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Check if the reply contains "loadrejected"
        assertTrue(reply.contains("loadrejected"))
        if(reply.contains("loadrejected"))
            ColorsOut.outappl("TEST TypesReply containing loadrejected: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST TypesReply containing loadrejected: FAILED", ColorsOut.RED);
    }
}