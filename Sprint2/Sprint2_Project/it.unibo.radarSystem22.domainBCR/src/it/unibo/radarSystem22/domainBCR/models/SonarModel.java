package it.unibo.radarSystem22.domainBCR.models;
 
import it.unibo.radarSystem22.domainBCR.Distance;
import it.unibo.radarSystem22.domainBCR.concrete.SonarConcreteHCSR04;
import it.unibo.radarSystem22.domainBCR.interfaces.*;
import it.unibo.radarSystem22.domainBCR.mock.SonarMock;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public abstract class SonarModel implements ISonar {
protected IDistance curVal = new Distance();
protected boolean stopped = true;
 	
	public static ISonar create()
	{
		if(DomainSystemConfig.simulation)
			return createSonarMock();
		else return createSonarConcrete();
	}

	public static ISonar createSonarMock()
	{
		ColorsOut.out("createSonarMock", ColorsOut.BLUE);
		return new SonarMock();
	}	
	public static ISonar createSonarConcrete()
	{
		ColorsOut.out("createSonarConcrete", ColorsOut.BLUE);
		return new SonarConcreteHCSR04();
	}

	// Hidden constructor, to force setup
	protected SonarModel()
	{
		ColorsOut.out("[" + this.getClass().getSimpleName() + "] calling (specialized) sonarSetUp ", ColorsOut.BLUE );
		sonarSetUp();   
	}
	
	protected void updateDistance(int d)
	{
		curVal = new Distance(d);
		ColorsOut.out("[" + this.getClass().getSimpleName() + "] updateDistance "+ d, ColorsOut.BLUE);
	}	

	protected abstract void sonarSetUp();
 	protected abstract void sonarProduce();

	@Override
	public boolean isActive()
	{
		//ColorsOut.out("SonarModel | isActive "+ (! stopped), ColorsOut.GREEN);
		return !stopped;
	}
	
	@Override
	public IDistance getDistance()
	{
		return curVal;
	}
	
	@Override
	public void activate()
	{
		curVal = new Distance();
 		ColorsOut.out("[" + this.getClass().getSimpleName() + "] activate" );
		stopped = false;
		new Thread() {
			public void run() {
				while(!stopped)
				{
					//Colors.out("SonarModel | call produce", Colors.GREEN);
					sonarProduce();
					//Utils.delay(RadarSystemConfig.sonarDelay);
				}
				ColorsOut.out("[" + this.getClass().getSimpleName() + "] ENDS" );
		    }
		}.start();
	}
 	
	@Override
	public void deactivate()
	{
		ColorsOut.out("[" + this.getClass().getSimpleName() + "] deactivate", ColorsOut.BgYellow );
		stopped = true;
	}

}

  

