package it.unibo.radarSystem22.domainBCR;

import static org.junit.Assert.assertTrue;

import it.unibo.radarSystem22.domainBCR.state.LedState;
import org.junit.*;

import it.unibo.radarSystem22.domainBCR.interfaces.ILed;
import it.unibo.radarSystem22.domainBCR.utils.BasicUtils;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public class TestLed {
 
	@Before
	public void up()
	{
		DomainSystemConfig.simulation = true; 
		DomainSystemConfig.ledGui     = true; 
	}
	
	@After
	public void down()
	{
		System.out.println("down");		
	}	
	
	@Test
	public void testLedMock()
	{
		System.out.println("testLedMock");
		
		ILed led = DeviceFactory.createLed();
		assertTrue(led.getState().equals(LedState.OFF));

		BasicUtils.delay(1000);

 		led.turnOn();
		assertTrue(led.getState().equals(LedState.ON));
		
		BasicUtils.delay(1000);
		
 		led.turnOff();
		assertTrue(led.getState().equals(LedState.OFF));
		
		BasicUtils.delay(1000);

		led.blink();
		assertTrue(led.getState().equals(LedState.BLINKING));

		BasicUtils.delay(1000);

		led.turnOff();
		assertTrue(led.getState().equals(LedState.OFF));

		BasicUtils.delay(3000);
	}	
	
	//@Test
	public void testAnotherLedMock()
	{
		ILed led = DeviceFactory.createLed();
		assertTrue(led.getState().equals(LedState.OFF));

		BasicUtils.delay(1000);
	}
	
	//@Test 
	public void testLedConcrete()
	{
		System.out.println("testLedConcrete");
		DomainSystemConfig.simulation = false; 
		
		ILed led = DeviceFactory.createLed();
		assertTrue(led.getState().equals(LedState.OFF));

		BasicUtils.delay(1000);
		
 		led.turnOn();
		assertTrue(led.getState().equals(LedState.ON));

		BasicUtils.delay(1000);

		led.turnOff();
		assertTrue(led.getState().equals(LedState.OFF));

		BasicUtils.delay(1000);

		led.blink();
		assertTrue(led.getState().equals(LedState.BLINKING));

		BasicUtils.delay(3000);
		
 		led.turnOff();
		assertTrue(led.getState().equals(LedState.OFF));

		BasicUtils.delay(3000);
	}
}
