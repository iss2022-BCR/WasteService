package it.unibo.radarSystem22.domainBCR.concrete;

import it.unibo.radarSystem22.domainBCR.interfaces.IBuzzer;
import it.unibo.radarSystem22.domainBCR.models.BuzzerModel;
import it.unibo.radarSystem22.domainBCR.state.BuzzerState;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * Class that implements a real buzzer component.
 */
public class BuzzerConcrete extends BuzzerModel implements IBuzzer {
    private Process process;

    @Override
    protected void buzzerActivate(BuzzerState val)
    {
        if(process != null && process.isAlive())
            process.destroy();

        ArrayList<String> command = new ArrayList<String>(Arrays.asList("/usr/bin/python3", "-u", "./buzzerBCR.py"));
        try {
            switch (val)
            {
                case ON:
                    command.add("on");
                    break;
                case OFF:
                    command.add("off");
                    break;
                case INTERMITTENT:
                    command.addAll(Arrays.asList("intermittent", "" + DomainSystemConfig.buzzerDelay));
                    break;
                default:
                    throw new IOException("Unknown Buzzer State: " + val);
            }

            ProcessBuilder builder = new ProcessBuilder(command);
            process = builder.start();

        } catch (IOException e) {
            System.out.println("[" + this.getClass().getSimpleName() + "] ERROR: " +  e.getMessage());
        }
    }
}
