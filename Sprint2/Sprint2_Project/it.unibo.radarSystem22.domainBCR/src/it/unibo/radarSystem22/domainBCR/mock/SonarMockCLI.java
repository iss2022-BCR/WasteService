package it.unibo.radarSystem22.domainBCR.mock;

import it.unibo.radarSystem22.domainBCR.Distance;
import it.unibo.radarSystem22.domainBCR.interfaces.IDistance;
import it.unibo.radarSystem22.domainBCR.interfaces.ISonar;
import it.unibo.radarSystem22.domainBCR.models.SonarModel;
import it.unibo.radarSystem22.domainBCR.utils.BasicUtils;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public class SonarMockCLI extends SonarModel implements ISonar {
	private int delta = 5;

	@Override
	protected void sonarSetUp()
	{
		curVal = new Distance(DomainSystemConfig.sonarDistanceMax);
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
		{
			//produces always the same value
			updateDistance(DomainSystemConfig.testingDistance);
			//stopped = true; //one shot
		}
		else
		{
			if(curVal.getVal() < 1)
			{
				updateDistance(DomainSystemConfig.sonarDistanceMax);
				ColorsOut.out("[" + this.getClass().getSimpleName() + "] distance = " + curVal.getVal() + " cm");
				return;
			}
			while(curVal.getVal() >= 1)
			{
				updateDistance(curVal.getVal() - delta);
				ColorsOut.out("[" + this.getClass().getSimpleName() + "] distance = " + curVal.getVal() + " cm");

				BasicUtils.delay(DomainSystemConfig.sonarDelay);
			}
		}
 	}
}
