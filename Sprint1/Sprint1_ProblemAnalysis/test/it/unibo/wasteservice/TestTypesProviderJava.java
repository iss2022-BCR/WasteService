package it.unibo.wasteservice;

import it.unibo.ctx_typesprovider_test.MainCtx_typesprovider_testKt;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.kactor.QakContext;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.tcp.TcpClientSupport;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommSystemConfig;
import unibo.comm22.utils.CommUtils;
import wasteservice.Utils;

import static org.junit.Assert.assertTrue;

public class TestTypesProviderJava {
    private final static String hostname = "localhost";
    private final static int port = 11701;
    private final static String actorName = "typesprovider";

    private Thread ctx_test;
    private ActorBasic actorTypesProvider;
    private Interaction2021 interaction;

    @Before
    public void startup() {
        CommSystemConfig.tracing = false;

        setupTypesProvider();

        try {
            interaction = TcpClientSupport.connect(hostname, port, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void shutdown() {
        actorTypesProvider.terminate(0);
        actorTypesProvider.terminateCtx(0);

        try {
            interaction.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ColorsOut.outappl("TEST TERMINATED", ColorsOut.CYAN);
    }

    private void setupTypesProvider() {
        new Thread(MainCtx_typesprovider_testKt::main).start();

        actorTypesProvider = QakContext.Companion.getActor(actorName);
        while(actorTypesProvider == null) {
            CommUtils.delay(500);
            actorTypesProvider = QakContext.Companion.getActor(actorName);
        }

        ColorsOut.outappl("[Test_WasteService] UP", ColorsOut.MAGENTA);
    }


    @org.junit.Test
    public void testLoadAcceptedPlastic() {
        try {
            IApplMessage msg = MsgUtil.buildRequest("smartdevice_test", "typesrequest", "typesrequest()", actorName);
            ColorsOut.outappl("[Test_SmartDevice] " + msg.toString(), ColorsOut.MAGENTA);

            String reply = interaction.request(msg.toString());

            assertTrue(reply.contains(Utils.INSTANCE.getWasteTypesString("_")));
            if(reply.contains(Utils.INSTANCE.getWasteTypesString("_")))
                ColorsOut.outappl("TEST TypesRequest: PASSED", ColorsOut.GREEN);
            else ColorsOut.outappl("TEST TypesRequest: FAILED", ColorsOut.RED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
