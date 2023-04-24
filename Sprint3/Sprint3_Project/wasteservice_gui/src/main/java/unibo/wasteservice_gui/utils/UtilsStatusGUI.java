package unibo.wasteservice_gui.utils;

import unibo.comm22.coap.CoapConnection;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.tcp.TcpClientSupport;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommSystemConfig;

public class UtilsStatusGUI {
    private static String ctxName = "ctx_statusgui";
    private static String actorName = "gui_updater";

    private static Interaction2021 conn;
    private static Interaction2021 connTCP;

    public static CoapConnection connectWithUtilsUsingCoap(String ip, int port){
        try{
            CommSystemConfig.tracing = true;
            String path = ctxName + "/" + actorName;
            conn = new CoapConnection(ip + ":" + port, path);
            ColorsOut.out("[UtilsStatusGUI] connect Tcp conn:" + conn );
            ColorsOut.outappl("[UtilsStatusGUI] connect Coap conn:" + conn , ColorsOut.CYAN);
        }catch(Exception e){
            ColorsOut.outerr("[UtilsStatusGUI] connect with GUIupdater ERROR:"+e.getMessage());
        }
        return (CoapConnection) conn;
    }

    public static void connectWithUtilsUsingTcp(String ip, int port){
        try {
            CommSystemConfig.tracing = true;
            connTCP = TcpClientSupport.connect(ip, port, 10);
            ColorsOut.out("[UtilsStatusGUI] connect Tcp conn:" + conn );
            ColorsOut.outappl("[UtilsStatusGUI] connect Tcp conn:" + conn , ColorsOut.CYAN);
        }catch(Exception e){
            ColorsOut.outerr("[UtilsStatusGUI] connect with GUIupdater ERROR:"+e.getMessage());
        }
    }


    public static void sendMsg() {
        try {
            String msg = "msg(get_data, dispatch, ws_gui, gui_updater, get_data(_), 1)";
            ColorsOut.outappl("[UtilsStatusGUI] sendMsg msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            connTCP.forward(msg);
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] sendMsg on:" + conn + " ERROR:" + e.getMessage());
        }
    }
}
