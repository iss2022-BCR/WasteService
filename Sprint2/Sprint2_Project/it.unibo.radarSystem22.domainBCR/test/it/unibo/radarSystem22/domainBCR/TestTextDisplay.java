package it.unibo.radarSystem22.domainBCR;

import it.unibo.radarSystem22.domainBCR.interfaces.ITextDisplay;
import it.unibo.radarSystem22.domainBCR.utils.BasicUtils;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

import org.junit.*;

import static org.junit.Assert.assertTrue;

public class TestTextDisplay {
    @Before
    public void up()
    {
        DomainSystemConfig.simulation   = true;
        DomainSystemConfig.textDisplay  = true;
    }

    @After
    public void down()
    {
        System.out.println("down");
    }

    @Test
    public void testTextDisplayMock()
    {
        System.out.println("testTextDisplayMock");

        ITextDisplay textDisplay = DeviceFactory.createTextDisplay();
        assertTrue(textDisplay.getLine1().isEmpty());
        assertTrue(textDisplay.getLine2().isEmpty());

        BasicUtils.delay(1000);

        String line1 = "Test line1";
        String line2 = "";
        textDisplay.setLines(line1, line2);
        assertTrue(textDisplay.getLine1().equals(line1));
        assertTrue(textDisplay.getLine2().equals(line2));

        BasicUtils.delay(1000);

        textDisplay.clear();
        assertTrue(textDisplay.getLine1().isEmpty());
        assertTrue(textDisplay.getLine2().isEmpty());

        BasicUtils.delay(1000);

        line1 = "";
        line2 = "Test line2";
        textDisplay.setLines(line1, line2);
        assertTrue(textDisplay.getLine1().isEmpty());
        assertTrue(textDisplay.getLine2().equals(line2));

        BasicUtils.delay(1000);

        textDisplay.clear();
        assertTrue(textDisplay.getLine1().isEmpty());
        assertTrue(textDisplay.getLine2().isEmpty());

        BasicUtils.delay(1000);

        line1 = "Test both1";
        line2 = "Test both2";
        textDisplay.setLines(line1, line2);
        assertTrue(textDisplay.getLine1().equals(line1));
        assertTrue(textDisplay.getLine2().equals(line2));

        BasicUtils.delay(1000);

        line1 = "Test changed1";
        line2 = "";
        textDisplay.setLines(line1, line2);
        assertTrue(textDisplay.getLine1().equals(line1));
        assertTrue(!textDisplay.getLine2().isEmpty());

        BasicUtils.delay(1000);

        line1 = "";
        line2 = "Test changed2";
        textDisplay.setLines(line1, line2);
        assertTrue(!textDisplay.getLine1().isEmpty());
        assertTrue(textDisplay.getLine2().equals(line2));

        BasicUtils.delay(3000);
    }

    @Test
    public void testUpdateDelay()
    {
        DomainSystemConfig.textLine1Delay = 2000;
        DomainSystemConfig.textLine2Delay = 50;

        ITextDisplay textDisplay = DeviceFactory.createTextDisplay();
        assertTrue(textDisplay.getLine1().isEmpty());
        assertTrue(textDisplay.getLine2().isEmpty());

        BasicUtils.delay(2000);

        String line1 = "Test line1";
        String line2 = "Test line2";
        textDisplay.setLines(line1, line2);
        assertTrue(textDisplay.getLine1().equals(line1));
        assertTrue(textDisplay.getLine2().equals(line2));

        BasicUtils.delay(500);

        // Test not updated (not enough time has passed
        line1 = "Test changed1";
        textDisplay.setLines(line1, "");
        assertTrue(!textDisplay.getLine1().equals(line1));

        // Time updated (enough time has passed)
        line2 = "Test changed2";
        textDisplay.setLines(line1, line2);
        assertTrue(!textDisplay.getLine1().equals(line1));
        assertTrue(textDisplay.getLine2().equals(line2));
    }

    //@Test
    public void testTextDisplayConcrete()
    {
        System.out.println("testTextDisplayConcrete");
        DomainSystemConfig.simulation = false;

        ITextDisplay textDisplay = DeviceFactory.createTextDisplay();
        assertTrue(textDisplay.getLine1().isEmpty() && textDisplay.getLine2().isEmpty());

        BasicUtils.delay(1000);

        String line1 = "Test line1";
        String line2 = "";
        textDisplay.setLines(line1, line2);
        assertTrue(textDisplay.getLine1().equals(line1) && textDisplay.getLine2().equals(line2));

        BasicUtils.delay(1000);

        textDisplay.clear();
        assertTrue(textDisplay.getLine1().isEmpty() && textDisplay.getLine2().isEmpty());

        BasicUtils.delay(1000);

        line1 = "";
        line2 = "Test line2";
        textDisplay.setLines(line1, line2);
        assertTrue(textDisplay.getLine1().isEmpty() && textDisplay.getLine2().equals(line2));

        BasicUtils.delay(1000);

        textDisplay.setLines("", "");
        assertTrue(textDisplay.getLine1().isEmpty() && textDisplay.getLine2().isEmpty());

        BasicUtils.delay(1000);

        line1 = "Test both1";
        line2 = "Test both2";
        textDisplay.setLines(line1, line2);
        assertTrue(textDisplay.getLine1().equals(line1) && textDisplay.getLine2().equals(line2));

        BasicUtils.delay(1000);

        line1 = "Test changed1";
        line2 = "";
        textDisplay.setLines(line1, line2);
        assertTrue(textDisplay.getLine1().equals(line1) && !textDisplay.getLine2().isEmpty());

        BasicUtils.delay(1000);

        line1 = "";
        line2 = "Test changed2";
        textDisplay.setLines(line1, line2);
        assertTrue(!textDisplay.getLine1().isEmpty() && textDisplay.getLine2().equals(line2));

        BasicUtils.delay(3000);
    }
}
