package it.unibo.radarSystem22.domainBCR;

import it.unibo.radarSystem22.domainBCR.concrete.RadarDisplay;
import it.unibo.radarSystem22.domainBCR.concrete.SonarConcreteForObs;
import it.unibo.radarSystem22.domainBCR.interfaces.*;
import it.unibo.radarSystem22.domainBCR.mock.SonarMockForObs;
import it.unibo.radarSystem22.domainBCR.models.LedModel;
import it.unibo.radarSystem22.domainBCR.models.SonarModel;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public class DeviceFactory {

	public static ILed createLed() {
		//Colors.out("DeviceFactory | createLed simulated="+RadarSystemConfig.simulation);
		if( DomainSystemConfig.simulation)  {
			return LedModel.createLedMock();
		}else {
			return LedModel.createLedConcrete();
		}
	}
	public static ISonarForObs createSonarForObs( ) {
		if( DomainSystemConfig.simulation)  {
			return new SonarMockForObs();
		}else { 
			return new SonarConcreteForObs();
		}
	}

	public static ISonar createSonar() {
		//Colors.out("DeviceFactory | createSonar simulated="+RadarSystemConfig.simulation);
		//if( DomainSystemConfig.simulation)  {
		//	return SonarModel.createSonarMock();
		//}else {
			return SonarModel.createSonarConcrete();
		//}
	}
 
	//We do not have mock for RadarGui
	public static IRadarDisplay createRadarGui() {
		return RadarDisplay.getRadarDisplay();
	}
	
}
