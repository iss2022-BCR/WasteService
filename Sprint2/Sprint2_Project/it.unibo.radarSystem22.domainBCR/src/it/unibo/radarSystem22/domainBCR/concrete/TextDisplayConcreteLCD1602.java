package it.unibo.radarSystem22.domainBCR.concrete;

import it.unibo.radarSystem22.domainBCR.interfaces.ITextDisplay;
import it.unibo.radarSystem22.domainBCR.models.TextDisplayModel;

import java.io.IOException;

/*
 * Class that implements a real LCD 1602 (16 characters 2 lines)
 * display component.
 */
public class TextDisplayConcreteLCD1602 extends TextDisplayModel implements ITextDisplay {
    @Override
    protected void textDisplayActivate(String text1, String text2)
    {
        String[] command = { "/usr/bin/python3", "-u", "./displayBCR.py", text1, text2 };
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch(IOException e) {
            System.out.println("[" + this.getClass().getSimpleName() + "] ERROR: " +  e.getMessage());
        }
    }
}
