package it.unibo.radarSystem22.domainBCR.models;

import it.unibo.radarSystem22.domainBCR.concrete.LedConcrete;
import it.unibo.radarSystem22.domainBCR.interfaces.*;
import it.unibo.radarSystem22.domainBCR.mock.LedMock;
import it.unibo.radarSystem22.domainBCR.mock.LedMockWithGui;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public abstract class LedModel implements ILed{
	private boolean state = false;	
	
	public static ILed create() {
		ILed led ; 
		if( DomainSystemConfig.simulation ) led = createLedMock();
		else led = createLedConcrete();
		return led;
	}
	
	public static ILed createLedMock() {
		if( DomainSystemConfig.ledGui ) return LedMockWithGui.createLed();
		else return new LedMock();
		
	}
	public static ILed createLedConcrete() {
		ColorsOut.out("createLedConcrete", ColorsOut.BLUE);
		return new LedConcrete();
	}	
	
	protected abstract void ledActivate( boolean val);
	
	protected void setState( boolean val ) {
		state = val;
		ledActivate( state );
	}
		
	@Override
	public void turnOn(){
		setState( true );
	}
	@Override
	public void turnOff() {
		setState(  false );		
	}
	@Override
	public boolean getState(){
		return state;
	}
	
}
