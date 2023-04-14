package it.unibo.radarSystem22.domainBCR.mock;

import it.unibo.radarSystem22.domainBCR.interfaces.ILed;
import it.unibo.radarSystem22.domainBCR.models.LedModel;
import it.unibo.radarSystem22.domainBCR.state.LedState;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;

public class LedMockCLI extends LedModel implements ILed {

	@Override
	protected void ledActivate(LedState val)
	{
		showState();
	}

	protected void showState()
	{
		ColorsOut.outappl("[" + this.getClass().getSimpleName() + "] State: " + getState(), ColorsOut.MAGENTA);
	}
} 