package it.unibo.radarSystem22.domainBCR.concrete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import it.unibo.radarSystem22.domainBCR.interfaces.ILed;
import it.unibo.radarSystem22.domainBCR.models.LedModel;
import it.unibo.radarSystem22.domainBCR.state.LedState;

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
					break;
				case OFF:
					command.add("off");
					break;
				case BLINKING:
					command.addAll(Arrays.asList("blink", "100"));
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
