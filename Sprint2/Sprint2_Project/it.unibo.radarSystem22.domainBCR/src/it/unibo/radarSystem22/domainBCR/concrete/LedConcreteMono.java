package it.unibo.radarSystem22.domainBCR.concrete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import it.unibo.radarSystem22.domainBCR.interfaces.ILed;
import it.unibo.radarSystem22.domainBCR.models.LedModel;
import it.unibo.radarSystem22.domainBCR.state.LedState;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

/*
* Class that implements a real monochromatic LED component.
 */
public class LedConcreteMono extends LedModel implements ILed {
	private Process process;

	@Override
	protected void ledActivate(LedState val)
	{
		if(process != null && process.isAlive())
			process.destroy();

		ArrayList<String> command = new ArrayList<String>(Arrays.asList("/usr/bin/python3", "-u", "./ledBCR.py"));
		try {
			switch (val)
			{
				case ON:
					command.add("on");
					ColorsOut.out("[" + this.getClass().getSimpleName() + "] state = ON");
					break;
				case OFF:
					command.add("off");
					ColorsOut.out("[" + this.getClass().getSimpleName() + "] state = OFF");
					break;
				case BLINKING:
					command.addAll(Arrays.asList("blink", "" + DomainSystemConfig.ledDelay));
					ColorsOut.out("[" + this.getClass().getSimpleName() + "] state = BLINKING");
					break;
				default:
					throw new IOException("Unknown Led State: " + val);
			}

			ProcessBuilder builder = new ProcessBuilder(command);
			process = builder.start();

		} catch (IOException e) {
			System.out.println("[" + this.getClass().getSimpleName() + "] ERROR: " +  e.getMessage());
		}
	}
}
