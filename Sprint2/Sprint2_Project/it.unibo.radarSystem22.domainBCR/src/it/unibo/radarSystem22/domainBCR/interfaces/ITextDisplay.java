package it.unibo.radarSystem22.domainBCR.interfaces;

public interface ITextDisplay extends IDevice {
    public void setLine1(String text);
    public void setLine2(String text);
    public void setLines(String text1, String text2);

    public String getLine1();
    public String getLine2();
    public void clear();
}