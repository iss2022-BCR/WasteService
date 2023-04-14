package it.unibo.radarSystem22.domainBCR.concrete;

import it.unibo.radarSystem22.domainBCR.interfaces.IBuzzer;
import it.unibo.radarSystem22.domainBCR.models.BuzzerModel;
import it.unibo.radarSystem22.domainBCR.state.BuzzerState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
                    command.addAll(Arrays.asList("intermittent", "100"));
                    break;
                default:
                    throw new IOException("Unknown Led State: " + val);
            }
        } catch (IOException e) {
            System.out.println("[" + this.getClass().getSimpleName() + "] ERROR: " +  e.getMessage());
        }
    }
}
