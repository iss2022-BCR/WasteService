package it.unibo.wasteservice

/*import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.QakContext
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.utils.ColorsOut
import unibo.comm22.utils.CommSystemConfig
import unibo.comm22.utils.CommUtils
import wasteservice.WasteType
import kotlin.concurrent.thread
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue*/

class TestDefectSolution {
    /*companion object {
        private const val hostname = "localhost"
        private const val port = 11705
        private const val actorNameSD1 = "smartdevice1"
        private const val actorNameSD2 = "smartdevice2"
        private const val actorNameWS = "wasteservice_ds_test"
        private const val actorNameTT = "transporttrolley_ds_test"
    }

    private var actorWasteService: ActorBasic? = null
    private var actorTransportTrolley: ActorBasic? = null
    private lateinit var smartDevice1: Interaction2021
    private lateinit var smartDevice2: Interaction2021

    private fun setupWasteServiceActor() {
        actorWasteService = QakContext.getActor(actorNameWS)
        while(actorWasteService == null) {
            CommUtils.delay(500)
            actorWasteService = QakContext.getActor(actorNameWS)
            println(actorWasteService)
        }
        ColorsOut.outappl("[WasteService] Actor correctly set up", ColorsOut.MAGENTA);
    }
    private fun setupTransportTrolleyActor() {
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

        ColorsOut.outappl("===== TEST Started =====", ColorsOut.CYAN);

        var thread = thread {
            RunCtxDefectSolutionTest().main()
        }
        setupWasteServiceActor();
        setupTransportTrolleyActor();

        // establish TCP connection (SmartDevice1 Test)
        try {
            smartDevice1 = TcpClientSupport.connect(Companion.hostname, port, 1);
            ColorsOut.outappl("[SmartDevice1] TCP communication established.", ColorsOut.MAGENTA);
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }

        CommUtils.delay(1000)
        // establish TCP connection (SmartDevice1 Test)
        try {
            smartDevice2 = TcpClientSupport.connect(Companion.hostname, port, 1);
            ColorsOut.outappl("[SmartDevice2] TCP communication established.", ColorsOut.MAGENTA);
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }
        CommUtils.delay(1000)
    }

    @AfterTest
    fun shutdown() {
        actorWasteService?.terminate(0);
        actorWasteService?.terminateCtx(0);

        try {
            smartDevice1.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        try {
            smartDevice2.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        CommUtils.delay(1000)
        ColorsOut.outappl("===== TEST Completed =====", ColorsOut.CYAN)
        CommUtils.delay(1000)
    }

    @Test
    fun testInstantLoadRejected() {
        // smartDevice1 sends a valid request
        ColorsOut.outappl("TEST: The load is accepted and the transport trolley start depositing.", ColorsOut.CYAN);
        val req: String = MsgUtil.buildRequest(actorNameSD1, "storerequest", "storerequest(GLASS, 10.0)", actorNameWS).toString()
        var reply: String = ""

        try {
            ColorsOut.outappl("[SmartDevice1] Sending a store request to $actorNameWS.", ColorsOut.MAGENTA);
            reply = smartDevice1.request(req)
            ColorsOut.outappl("[SmartDevice1] Received reply: $reply.", ColorsOut.MAGENTA);
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Checking if the reply contains all the WasteTypes
        for(type in WasteType.values()) {
            assertTrue(reply.contains(type.name))
            if(reply.contains(type.name))
                ColorsOut.outappl("TEST TypesReply containing ${type.name}: PASSED", ColorsOut.GREEN);
            else ColorsOut.outappl("TEST TypesReply containing ${type.name}: FAILED", ColorsOut.RED);
        }

        // smartDevice2 sends an invalid request and waits
        /*ColorsOut.outappl("TEST: The load is accepted and the transport trolley start depositing.", ColorsOut.CYAN);
        val req: String = MsgUtil.buildRequest(actorNameSD1, "storerequest", "storerequest(GLASS, 10.0)", actorNameWS).toString()
        var reply: String = ""

        try {
            ColorsOut.outappl("[SmartDevice1] Sending a store request to $actorNameWS.", ColorsOut.MAGENTA);
            reply = smartDevice1.request(req)
            ColorsOut.outappl("[SmartDevice1] Received reply: $reply.", ColorsOut.MAGENTA);
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
    }

    private fun simulateStoreRequest(interaction: Interaction2021, wType: WasteType, wWeight: Double): String {
        val request = MsgUtil.buildRequest(
            actorNameWS, "storerequest",
            "storerequest($wType, $wWeight)", actorNameWS).toString()
        var reply = ""

        try {
            reply = interaction.request(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return reply
    }*/
}