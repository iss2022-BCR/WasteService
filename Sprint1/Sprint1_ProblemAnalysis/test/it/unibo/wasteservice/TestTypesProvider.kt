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
import kotlin.test.assertTrue

/*
 * Test: check that the sent list is the same as the received one
 */
class TestTypesProvider {
    companion object {
        private const val hostname = "localhost"
        private const val port = 11701
        private const val actorNameSD = "smartdevice_tp_test"
        private const val actorNameTP = "typesprovider_tp_test"
    }

    private var actorTypesProvider: ActorBasic? = null
    private lateinit var interaction: Interaction2021

    private fun setupTypesProvider() {
        actorTypesProvider = QakContext.getActor(actorNameTP)
        while(actorTypesProvider == null) {
            CommUtils.delay(500)
            actorTypesProvider = QakContext.getActor(actorNameTP)
            println(actorTypesProvider)
        }
        ColorsOut.outappl("[TypesProvider] Actor correctly set up", ColorsOut.MAGENTA);
    }

    @BeforeTest
    fun startup() {
        CommSystemConfig.tracing = false

        ColorsOut.outappl("===== TEST Started =====", ColorsOut.CYAN);

        var thread = thread {
            RunCtxTypesProviderTest().main()
        }
        setupTypesProvider();

        // establish TCP connection (SmartDevice Test)
        try {
            interaction = TcpClientSupport.connect(Companion.hostname, port, 1);
            ColorsOut.outappl("[SmartDevice] TCP communication established.", ColorsOut.MAGENTA);
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }
        CommUtils.delay(1000)
    }

    @AfterTest
    fun shutdown() {
        actorTypesProvider?.terminate(0);
        actorTypesProvider?.terminateCtx(0);

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
    fun testCorrectTypes() {
        ColorsOut.outappl("TEST: The reply received from $actorNameTP contains all the WasteType available.", ColorsOut.CYAN);
        val req: String = MsgUtil.buildRequest(actorNameSD, "typesrequest", "typesrequest()", actorNameTP).toString()
        var reply: String = ""

        try {
            ColorsOut.outappl("[SmartDevice] Sending typesrequest to $actorNameTP.", ColorsOut.MAGENTA);
            reply = interaction.request(req)
            ColorsOut.outappl("[SmartDevice] Received reply: $reply.", ColorsOut.MAGENTA);
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
    }

    /*@Test
    fun testListFormat() {
        ColorsOut.outappl("TEST: The reply contains a list in the expected format", ColorsOut.CYAN);
        val req: String = MsgUtil.buildRequest(actorNameSD, "typesrequest", "typesrequest()", actorName).toString()
        var reply: String = ""

        try {
            ColorsOut.outappl("[SmartDevice] Sending typesrequest to $actorName.", ColorsOut.MAGENTA);
            reply = interaction.request(req)
            ColorsOut.outappl("[SmartDevice] Received reply: $reply.", ColorsOut.MAGENTA);
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Checking if the reply contains a properly formatted list
        assertTrue(reply.contains(utils.getWasteTypesString()))
        if(reply.contains(utils.getWasteTypesString()))
            ColorsOut.outappl("TEST TypesReply containing properly formatted list: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST TypesReply containing properly formatted list: FAILED", ColorsOut.RED);
    }*/
}