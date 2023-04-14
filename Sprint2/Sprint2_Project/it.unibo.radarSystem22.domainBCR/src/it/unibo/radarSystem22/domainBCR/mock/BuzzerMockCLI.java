package it.unibo.radarSystem22.domainBCR.mock;

import it.unibo.radarSystem22.domainBCR.interfaces.IBuzzer;
import it.unibo.radarSystem22.domainBCR.models.BuzzerModel;
import it.unibo.radarSystem22.domainBCR.state.BuzzerState;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;

public class BuzzerMockCLI extends BuzzerModel implements IBuzzer {

    @Override
    protected void buzzerActivate(BuzzerState val)
    {
        showState();
    }

    protected void showState()
    {
        ColorsOut.outappl("[" + this.getClass().getSimpleName() + "] State: " + getState(), ColorsOut.MAGENTA);
    }
}
