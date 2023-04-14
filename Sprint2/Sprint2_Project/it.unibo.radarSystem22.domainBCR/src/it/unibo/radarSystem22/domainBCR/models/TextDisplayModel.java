package it.unibo.radarSystem22.domainBCR.models;

import it.unibo.radarSystem22.domainBCR.concrete.TextDisplayConcreteLCD1602;
import it.unibo.radarSystem22.domainBCR.interfaces.ITextDisplay;
import it.unibo.radarSystem22.domainBCR.mock.TextDisplayMockCLI;
import it.unibo.radarSystem22.domainBCR.mock.TextDisplayMockGUI;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public abstract class TextDisplayModel implements ITextDisplay {
    private String text1 = "";
    private String text2 = "";

    public static ITextDisplay create()
    {
        return DomainSystemConfig.simulation ?
                createTextDisplayMock() : createTextDisplayMock();
    }

    public static ITextDisplay createTextDisplayMock()
    {
        return DomainSystemConfig.textDisplay ?
                TextDisplayMockGUI.createTextDisplay() : new TextDisplayMockCLI();
    }

    public static ITextDisplay createTextDisplayConcrete()
    {
        ColorsOut.out("createTextDisplayConcrete", ColorsOut.BLUE);
        return new TextDisplayConcreteLCD1602();
    }

    protected abstract void textDisplayActivate(String text1, String text2);

    @Override
    public void setLines(String text1, String text2)
    {
        if(!text1.isEmpty())
            this.text1 = text1;

        if(!text2.isEmpty())
            this.text2 = text2;

        if(text1.isEmpty() && text2.isEmpty())
            clear();

        textDisplayActivate(text1, text2);
    }

    @Override
    public String getLine1()
    {
        return text1;
    }

    @Override
    public String getLine2()
    {
        return text2;
    }

    @Override
    public void clear()
    {
        text1 = "";
        text2 = "";
    }
}
