package it.unibo.radarSystem22.domainBCR.concrete;

import java.io.IOException;

import it.unibo.radarSystem22.domainBCR.interfaces.ILed;
import it.unibo.radarSystem22.domainBCR.models.LedModel;

public class LedConcrete extends LedModel implements ILed{
private Runtime rt  = Runtime.getRuntime();
 	
	@Override
	protected void ledActivate(boolean val) {
		try {
			if( val ) rt.exec( "sudo bash led25GpioTurnOn.sh" );
			else rt.exec( "sudo bash led25GpioTurnOff.sh" );
		} catch (IOException e) {
			System.out.println("LedConcrete | ERROR " +  e.getMessage());
		}
	}
}
