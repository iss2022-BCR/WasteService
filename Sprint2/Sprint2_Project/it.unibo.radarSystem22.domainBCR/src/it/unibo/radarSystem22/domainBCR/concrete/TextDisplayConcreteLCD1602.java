package it.unibo.radarSystem22.domainBCR.concrete;

import it.unibo.radarSystem22.domainBCR.interfaces.ITextDisplay;
import it.unibo.radarSystem22.domainBCR.models.TextDisplayModel;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;

import java.io.IOException;

/*
 * Class that implements a real LCD 1602 (16 characters 2 lines)
 * display component with the I2C Expander bus module.
 */
public class TextDisplayConcreteLCD1602 extends TextDisplayModel implements ITextDisplay {
    @Override
    protected void textDisplayActivate(String text1, String text2)
    {
        String[] command = { "/usr/bin/python3", "-u", "./displayBCR.py", getLine1(), getLine2()};
        ProcessBuilder builder = new ProcessBuilder(command);

        String className = "[" + this.getClass().getSimpleName() + "]";
        ColorsOut.out(className + " Line1: " + getLine1());
        ColorsOut.out(className.replaceAll("(?s)(?<!\\\\S).(?!\\\\S)", " ") + " Line2: " + getLine2());

        try {
            builder.start();
        } catch(IOException e) {
            System.out.println("[" + this.getClass().getSimpleName() + "] ERROR: " +  e.getMessage());
        }
    }
}
