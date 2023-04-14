package it.unibo.radarSystem22.domainBCR.mock;

import it.unibo.radarSystem22.domainBCR.Distance;
import it.unibo.radarSystem22.domainBCR.interfaces.IDistance;
import it.unibo.radarSystem22.domainBCR.interfaces.ISonar;
import it.unibo.radarSystem22.domainBCR.models.SonarModel;
import it.unibo.radarSystem22.domainBCR.utils.BasicUtils;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public class SonarMock extends SonarModel implements ISonar {
private int delta = 5;
	@Override
	protected void sonarSetUp()
	{
		curVal = new Distance(90);		
		ColorsOut.out("[" + this.getClass().getSimpleName() + "] sonarSetUp curVal="+curVal);
	}
	
	@Override
	public IDistance getDistance()
	{
		return curVal;
	}

	@Override
	protected void sonarProduce()
	{
		if(DomainSystemConfig.testing)
		{	//produces always the same value
			updateDistance(DomainSystemConfig.testingDistance);
			//stopped = true; //one shot
		}
		else
		{
			int v = curVal.getVal() - delta;
			updateDistance(v);
			stopped = (v <= 0);
		}
		BasicUtils.delay(DomainSystemConfig.sonarDelay);  //avoid fast generation
 	}
}
