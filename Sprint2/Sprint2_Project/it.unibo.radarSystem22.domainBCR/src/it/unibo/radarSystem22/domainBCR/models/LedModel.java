package it.unibo.radarSystem22.domainBCR.models;

import it.unibo.radarSystem22.domainBCR.concrete.LedConcreteMono;
import it.unibo.radarSystem22.domainBCR.interfaces.*;
import it.unibo.radarSystem22.domainBCR.mock.LedMockCLI;
import it.unibo.radarSystem22.domainBCR.mock.LedMockGUI;
import it.unibo.radarSystem22.domainBCR.state.LedState;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public abstract class LedModel implements ILed {
	private LedState state = LedState.OFF;
	
	public static ILed create()
	{
		return DomainSystemConfig.simulation ?
				createLedMock() : createLedConcrete();
	}
	
	public static ILed createLedMock()
	{
		return DomainSystemConfig.ledGui ?
				LedMockGUI.createLed() : new LedMockCLI();
	}

	public static ILed createLedConcrete()
	{
		ColorsOut.out("createLedConcrete", ColorsOut.BLUE);
		return new LedConcreteMono();
	}	
	
	protected abstract void ledActivate(LedState val);
	
	protected void setState(LedState val)
	{
		state = val;
		ledActivate(state);
	}
		
	@Override
	public void turnOn()
	{
		setState(LedState.ON);
	}

	@Override
	public void turnOff()
	{
		setState(LedState.OFF);
	}

	@Override
	public void blink()
	{
		setState(LedState.BLINKING);
	}

	@Override
	public LedState getState()
	{
		return state;
	}
	
}
