package it.unibo.wasteservice.sprint0;

import it.unibo.kactor.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import static org.junit.Assert.assertTrue;
import it.unibo.ctx_test_storerequest.MainCtx_test_storerequestKt;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommSystemConfig;
import unibo.comm22.utils.CommUtils;
import it.unibo.kactor.MsgUtil;
import wasteservice.Constants;
import wasteservice.WasteType;
import unibo.comm22.tcp.TcpClientSupport;

public class TestInteractions {
    private final static String hostname = "localhost";
    private final static int port = 11820;
    private final static String actorName = "test_wasteservice";

    private Thread ctx_test;
    private ActorBasic actorWasteService;
    private Interaction2021 interaction;

    @Before
    public void startup() {
        CommSystemConfig.tracing = false;

        setupWasteService();

        try {
            interaction = TcpClientSupport.connect(hostname, port, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    public void shutdown() {
        actorWasteService.terminate(0);
        actorWasteService.terminateCtx(0);

        try {
            interaction.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ColorsOut.outappl("TEST TERMINATED", ColorsOut.CYAN);
    }

    private void setupWasteService() {
        new Thread(MainCtx_test_storerequestKt::main).start();

        actorWasteService = QakContext.Companion.getActor(actorName);
        while(actorWasteService == null) {
            CommUtils.delay(500);
            actorWasteService = QakContext.Companion.getActor(actorName);
        }

        ColorsOut.outappl("[Test_WasteService] UP", ColorsOut.MAGENTA);
    }


    @Test
    public void testLoadAcceptedPlastic() {
        try {
            IApplMessage msg = MsgUtil.buildRequest("testPlasticOK", "storerequest", "storerequest(" + WasteType.PLASTIC + ", " + (Constants.MAXPB / 3) + ")", actorName);
            ColorsOut.outappl("[Test_SmartDevice] " + msg.toString(), ColorsOut.MAGENTA);

            String reply = interaction.request(msg.toString());

            assertTrue(reply.contains("loadaccepted"));
            if(reply.contains("loadaccepted"))
                ColorsOut.outappl("TEST LoadAccepted: PASSED", ColorsOut.GREEN);
            else ColorsOut.outappl("TEST LoadAccepted: FAILED", ColorsOut.RED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadAcceptedGlass() {
        try {
            IApplMessage msg = MsgUtil.buildRequest("testPlasticFAIL", "storerequest", "storerequest(" + WasteType.GLASS + ", " + (Constants.MAXGB / 2) + ")", actorName);
            ColorsOut.outappl("[Test_SmartDevice] " + msg.toString(), ColorsOut.MAGENTA);

            String reply = interaction.request(msg.toString());

            assertTrue(reply.contains("loadaccepted"));
            if(reply.contains("loadaccepted"))
                ColorsOut.outappl("TEST LoadAccepted: PASSED", ColorsOut.GREEN);
            else ColorsOut.outappl("TEST LoadAccepted: FAILED", ColorsOut.RED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadRejectedPlastic() {
        try {
            IApplMessage msg = MsgUtil.buildRequest("testGlassOK", "storerequest", "storerequest(" + WasteType.PLASTIC + ", " + (Constants.MAXPB + 10.0f) + ")", actorName);
            ColorsOut.outappl("[Test_SmartDevice] " + msg.toString(), ColorsOut.MAGENTA);

            String reply = interaction.request(msg.toString());

            assertTrue(reply.contains("loadrejected"));
            if(reply.contains("loadrejected"))
                ColorsOut.outappl("TEST LoadRejected: PASSED", ColorsOut.GREEN);
            else ColorsOut.outappl("TEST LoadRejected: FAILED", ColorsOut.RED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadRejectedGlass() {
        try {
            IApplMessage msg = MsgUtil.buildRequest("testGlassFAIL", "storerequest", "storerequest(" + WasteType.GLASS + ", " + (Constants.MAXGB + 23.3f) + ")", actorName);
            ColorsOut.outappl("[Test_SmartDevice] " + msg.toString(), ColorsOut.MAGENTA);

            String reply = interaction.request(msg.toString());

            assertTrue(reply.contains("loadrejected"));
            if(reply.contains("loadrejected"))
                ColorsOut.outappl("TEST LoadRejected: PASSED", ColorsOut.GREEN);
            else ColorsOut.outappl("TEST LoadRejected: FAILED", ColorsOut.RED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
