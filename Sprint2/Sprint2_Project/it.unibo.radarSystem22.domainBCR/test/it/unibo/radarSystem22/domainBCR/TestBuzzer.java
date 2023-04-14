package it.unibo.radarSystem22.domainBCR;

import static org.junit.Assert.assertTrue;

import it.unibo.radarSystem22.domainBCR.interfaces.IBuzzer;
import it.unibo.radarSystem22.domainBCR.state.BuzzerState;
import it.unibo.radarSystem22.domainBCR.state.LedState;
import it.unibo.radarSystem22.domainBCR.utils.BasicUtils;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;
import org.junit.*;

public class TestBuzzer {

    @Before
    public void up()
    {
        DomainSystemConfig.simulation   = true;
        DomainSystemConfig.buzzerSound  = true;
    }

    @After
    public void down()
    {
        System.out.println("down");
    }

    @Test
    public void testBuzzerMocK()
    {
        System.out.println("testBuzzerMock");

        IBuzzer buzzer = DeviceFactory.createBuzzer();
        assertTrue(buzzer.getState().equals(BuzzerState.OFF));

        BasicUtils.delay(1000);

        buzzer.turnOn();
        assertTrue(buzzer.getState().equals(BuzzerState.ON));

        BasicUtils.delay(1000);

        buzzer.turnOff();
        assertTrue(buzzer.getState().equals(BuzzerState.OFF));

        BasicUtils.delay(1000);

        buzzer.turnIntermittent();
        assertTrue(buzzer.getState().equals(BuzzerState.INTERMITTENT));

        BasicUtils.delay(1000);

        buzzer.turnOff();
        assertTrue(buzzer.getState().equals(BuzzerState.OFF));

        BasicUtils.delay(3000);
    }

    //@Test
    public void testBuzzerConcrete()
    {
        System.out.println("testBuzzerConcrete");
        DomainSystemConfig.simulation = false;

        IBuzzer buzzer = DeviceFactory.createBuzzer();
        assertTrue(buzzer.getState().equals(BuzzerState.OFF));

        BasicUtils.delay(1000);

        buzzer.turnOn();
        assertTrue(buzzer.getState().equals(BuzzerState.ON));

        BasicUtils.delay(1000);

        buzzer.turnOff();
        assertTrue(buzzer.getState().equals(BuzzerState.OFF));

        BasicUtils.delay(1000);

        buzzer.turnIntermittent();
        assertTrue(buzzer.getState().equals(BuzzerState.INTERMITTENT));

        BasicUtils.delay(1000);

        buzzer.turnOff();
        assertTrue(buzzer.getState().equals(BuzzerState.OFF));

        BasicUtils.delay(3000);
    }
}
