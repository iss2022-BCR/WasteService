package it.unibo.radarSystem22.domainBCR.interfaces;

public interface IDisplay extends IDevice {
    public void setLine1(String text);
    public void setLine2(String text);
    public void setLines(String text1, String text2);

    public void getLine1();
    public void getLine2();
    public void clear();
}
