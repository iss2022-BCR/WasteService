package it.unibo.wasteservice

import it.unibo.kactor.ActorBasic
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

/**
 * Container Update Test
 *
 * 1. Send storerequest
 * 2. Wait for CoAP update
 * 3. Check CoAP resource content
 */
class TestContainerUpdate {
    companion object {
        private const val hostname = "localhost"
        private const val port = 11800
        private const val actorNameSD = "smartdevice"
        private const val actorNameWS = "wasteservice"
        private const val actorNameSC = "status_controller"
        const val ctxName = "ctx_wasteservice"
    }

    private var actorWasteService: ActorBasic? = null
    private var actorStatusController: ActorBasic? = null

    private lateinit var obs: CoapObserver
    private lateinit var interaction: Interaction2021

    private fun setup() {
        actorWasteService = QakContext.getActor(actorNameWS)
        while(actorWasteService == null) {
            CommUtils.delay(500)
            actorWasteService = QakContext.getActor(actorNameWS)
            println(actorWasteService)
        }
        ColorsOut.outappl("[WasteService] Actor correctly set up", ColorsOut.MAGENTA);

        actorStatusController = QakContext.getActor(actorNameSC)
        while(actorStatusController == null) {
            CommUtils.delay(500)
            actorStatusController = QakContext.getActor(actorNameSC)
            println(actorStatusController)
        }
        ColorsOut.outappl("[StatusController] Actor correctly set up", ColorsOut.MAGENTA);

        var actorTmp = QakContext.getActor("transporttrolley")
        while(actorTmp == null) {
            CommUtils.delay(500)
            actorTmp = QakContext.getActor("transporttrolley")
            println(actorTmp)
        }
        ColorsOut.outappl("[TransportTrolley] Actor correctly set up", ColorsOut.MAGENTA);

        actorTmp = QakContext.getActor("trolleystateprovider")
        while(actorTmp == null) {
            CommUtils.delay(500)
            actorTmp = QakContext.getActor("trolleystateprovider")
            println(actorTmp)
        }
        ColorsOut.outappl("[TrolleyStateProvider] Actor correctly set up", ColorsOut.MAGENTA);

        actorTmp = QakContext.getActor("pathexecutorbcr")
        while(actorTmp == null) {
            CommUtils.delay(500)
            actorTmp = QakContext.getActor("pathexecutorbcr")
            println(actorTmp)
        }
        ColorsOut.outappl("[PathExceutorBCR] Actor correctly set up", ColorsOut.MAGENTA);

        actorTmp = QakContext.getActor("basicrobot")
        while(actorTmp == null) {
            CommUtils.delay(500)
            actorTmp = QakContext.getActor("basicrobot")
            println(actorTmp)
        }
        ColorsOut.outappl("[BasicRobot] Actor correctly set up", ColorsOut.MAGENTA);
    }

    @BeforeTest
    fun startup() {
        CommSystemConfig.tracing = false

        ColorsOut.outappl("===== TEST Started =====", ColorsOut.CYAN);

        obs = CoapObserver()

        thread {
            RunCtxWasteServiceTest().main()
        }
        thread {
            RunCtxTransportTrolleyTest().main()
        }
        thread {
            RunCtxRobotTest().main()
        }
        thread {
            RunCtxStatusGuiTest().main()
        }
        setup();

        obs.addContext(ctxName, Pair(hostname, port))
        obs.addActor(actorNameSC, ctxName)

        obs.createCoapConnection(actorNameSC)

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
        actorWasteService?.terminate(0);
        actorStatusController?.terminate(0);
        actorStatusController?.terminateCtx(0);

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
    fun testContainerUpdate() {
        var wasteType = WasteType.PLASTIC;
        var wasteWeight = 10.0

        ColorsOut.outappl("TEST: The StatusController updates the GUI with the correct waste from the correct container.", ColorsOut.CYAN);
        val req: String = MsgUtil.buildRequest(actorNameSD, "storerequest", "storerequest(${wasteType.name}, ${wasteWeight})", actorNameWS).toString()
        var reply: String = ""

        try {
            ColorsOut.outappl("[SmartDevice] Sending storerequest to ${actorNameWS}.", ColorsOut.MAGENTA);
            reply = interaction.request(req)
            ColorsOut.outappl("[SmartDevice] Received reply: $reply.", ColorsOut.MAGENTA);
        } catch (e: Exception) {
            e.printStackTrace()
        }

        obs.waitNextEntry(timeout= 5000)

        println(obs.getCoapHistory())


        // Check if the reply contains "loadaccepted"

    }
}