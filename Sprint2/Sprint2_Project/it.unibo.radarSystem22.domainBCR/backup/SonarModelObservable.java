package it.unibo.radarSystem22.domainBCR.models;

import it.unibo.radarSystem22.domainBCR.Distance;
import it.unibo.radarSystem22.domainBCR.concrete.SonarConcreteForObs;
import it.unibo.radarSystem22.domainBCR.interfaces.IDistance;
import it.unibo.radarSystem22.domainBCR.interfaces.IDistanceMeasured;
import it.unibo.radarSystem22.domainBCR.interfaces.IObserver;
import it.unibo.radarSystem22.domainBCR.interfaces.ISonarForObs;
import it.unibo.radarSystem22.domainBCR.mock.SonarMockForObs;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public abstract class SonarModelObservable extends SonarModel implements ISonarForObs  {
	protected IDistanceMeasured observableDistance ; 
 		
//	public static ISonarObservable create() {
//		if( DomainSystemConfig.simulation )  return new SonarMockForObs();
//		else  return new SonarConcreteForObs();		
//	}
	
	@Override
	public IDistance getDistance() {
  		return observableDistance;
 	}
	@Override
	protected void updateDistance( int d ) {
		//ColorsOut.out("SonarModelObservable call updateDistance | d=" + d, ColorsOut.GREEN );
 		observableDistance.setVal( new Distance( d ) ); //notifies
 	}	
  	@Override
	public void register(IObserver obs) {
		ColorsOut.out("SonarModelObservable | register on observableDistance obs="+obs, ColorsOut.GREEN);
		observableDistance.addObserver(obs);		
	}
	@Override
	public void unregister(IObserver obs) {
		ColorsOut.out("SonarModelObservable | unregister obs="+obs, ColorsOut.GREEN);
		observableDistance.deleteObserver(obs);		
	}

  
}
