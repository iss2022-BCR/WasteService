package it.unibo.radarSystem22.domainBCR;
import static org.junit.Assert.assertTrue;

import org.junit.*;

import it.unibo.radarSystem22.domainBCR.interfaces.ISonar;
import it.unibo.radarSystem22.domainBCR.utils.BasicUtils;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;
 

public class TestSonar {
	@Before
	public void up() {
		System.out.println("up");
	}
	
	@After
	public void down() {
		System.out.println("down");		
	}	
	
	//@Test
	public void testSonarMock() {
		DomainSystemConfig.simulation = true;
		DomainSystemConfig.testing    = false;
		DomainSystemConfig.sonarDelay = 10;		//quite fast generation ...
		int delta = 1;
		
		ISonar sonar = DeviceFactory.createSonar();
		new SonarConsumerForTesting( sonar, delta ).start();  //consuma
		sonar.activate();		
 		while( sonar.isActive() ) { BasicUtils.delay(1000);} //to avoid premature exit
 	}

	 @Test
	public void testSonarMockCLI()
	{
		DomainSystemConfig.simulation = true;
		DomainSystemConfig.testing    = false;
		DomainSystemConfig.sonarDelay = 50;		//quite fast generation ...

		int delta = 5;
		ISonar sonar = DeviceFactory.createSonar();

		new Thread() {
			public void run() {
				BasicUtils.delay(4000);
				sonar.deactivate();
			}
		}.start();
		sonar.activate();

		int prev = DomainSystemConfig.sonarDistanceMax;
		while(sonar.isActive()) {
			int curr = sonar.getDistance().getVal();
			if(curr < 1)
				curr = prev = DomainSystemConfig.sonarDistanceMax;
			if(curr != prev)
			{
				assertTrue(curr == prev - delta);
				prev = curr;
			}
			else BasicUtils.delay(20);
		}

		BasicUtils.delay(2000);
	}
}
