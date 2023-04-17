package it.unibo.radarSystem22.domainBCR.models;
 
import it.unibo.radarSystem22.domainBCR.Distance;
import it.unibo.radarSystem22.domainBCR.concrete.SonarConcreteForObs;
import it.unibo.radarSystem22.domainBCR.interfaces.IDistance;
import it.unibo.radarSystem22.domainBCR.interfaces.IDistanceMeasured;
import it.unibo.radarSystem22.domainBCR.interfaces.ISonar;
import it.unibo.radarSystem22.domainBCR.mock.SonarMockForObs;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public abstract class SonarModelForObs extends SonarModel  {
	protected IDistanceMeasured observableDistance ; 
 		
	public static ISonar   create() {
		if( DomainSystemConfig.simulation )  return new SonarMockForObs();
		else  return new SonarConcreteForObs();		
	}
	
	@Override
	public IDistance getDistance() {
  		return observableDistance;
 	}
	@Override
	protected void updateDistance( int d ) {
		//ColorsOut.out("SonarModelObservable call updateDistance | d=" + d, ColorsOut.GREEN );
 		observableDistance.setVal( new Distance( d ) ); //notifies
 	}	
 
  
}
