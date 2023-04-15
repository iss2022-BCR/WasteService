package it.unibo.radarSystem22.domainBCR.mock;

import it.unibo.radarSystem22.domainBCR.interfaces.ITextDisplay;
import it.unibo.radarSystem22.domainBCR.models.TextDisplayModel;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;

public class TextDisplayMockCLI extends TextDisplayModel implements ITextDisplay {
    @Override
    protected void textDisplayActivate(String text1, String text2)
    {
        showText();
    }

    protected void showText()
    {
        String className = "[" + this.getClass().getSimpleName() + "]";
        ColorsOut.out(className + " Line1: " + getLine1());
        ColorsOut.out(className.replaceAll("(?s)(?<!\\\\S).(?!\\\\S)", " ") + " Line2: " + getLine2());
    }
}
