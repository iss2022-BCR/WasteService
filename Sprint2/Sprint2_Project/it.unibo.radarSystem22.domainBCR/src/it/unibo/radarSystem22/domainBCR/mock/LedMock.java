package it.unibo.radarSystem22.domainBCR.mock;

import it.unibo.radarSystem22.domainBCR.interfaces.ILed;
import it.unibo.radarSystem22.domainBCR.models.LedModel;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;

public class LedMock extends LedModel implements ILed{

	@Override
	protected void ledActivate(boolean val) {	
		showState();
	}

	protected void showState(){
		ColorsOut.outappl("LedMock state=" + getState(), ColorsOut.MAGENTA );
	}
} 