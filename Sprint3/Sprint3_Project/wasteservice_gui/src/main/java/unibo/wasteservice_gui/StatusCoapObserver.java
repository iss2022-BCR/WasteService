package unibo.wasteservice_gui;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import unibo.comm22.utils.ColorsOut;
import unibo.wasteservice_gui.websocket.WebSocketConfiguration;

public class StatusCoapObserver implements CoapHandler {
    @Override
    public void onLoad(CoapResponse response) {
        ColorsOut.outappl("StatusCoapObserver changed!" + response.getResponseText(), ColorsOut.GREEN);
        WebSocketConfiguration.webSocketHandler.sendToAll(response.getResponseText());
    }

    @Override
    public void onError() {
        ColorsOut.outerr("StatusCoapObserver observe error!");
    }
}
