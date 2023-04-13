package it.unibo.radarSystem22.domainBCR.mock;

import it.unibo.radarSystem22.domainBCR.Distance;
import it.unibo.radarSystem22.domainBCR.DistanceMeasured;
import it.unibo.radarSystem22.domainBCR.interfaces.IDistanceMeasured;
import it.unibo.radarSystem22.domainBCR.interfaces.ISonarForObs;
import it.unibo.radarSystem22.domainBCR.models.SonarModel;
import it.unibo.radarSystem22.domainBCR.utils.BasicUtils;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;
 

public class SonarMockForObs extends SonarModel implements ISonarForObs    {
	protected IDistanceMeasured observableDistance ; 

	@Override
	public IDistanceMeasured getDistance() {
		return observableDistance;
	}
	@Override
	protected void sonarSetUp() {
		observableDistance = new DistanceMeasured( );		
 		ColorsOut.out("SonarMockForObs | sonarSetUp curVal="+curVal);
 		observableDistance.setVal(   curVal );
 	} 	
	@Override
	protected void updateDistance( int d ) {
		//ColorsOut.out("SonarModelObservable call updateDistance | d=" + d, ColorsOut.GREEN );
 		observableDistance.setVal( new Distance( d ) ); //notifies
 	}	
 
	@Override 
	protected void sonarProduce() {
		if( DomainSystemConfig.testing ) {
			updateDistance( DomainSystemConfig.testingDistance );			      
			stopped = true;  //one shot
		}else {
			int v = observableDistance.getVal() - 1; //curVal.getVal() - 1; 
			ColorsOut.out("SonarMockForObs | produced:"+v);
			updateDistance( v );			    
 			stopped = ( v == 0 );
 			curVal  =  new Distance( v ) ;
			BasicUtils.delay(DomainSystemConfig.sonarDelay);  //avoid fast generation
		}		
	}


 
}
