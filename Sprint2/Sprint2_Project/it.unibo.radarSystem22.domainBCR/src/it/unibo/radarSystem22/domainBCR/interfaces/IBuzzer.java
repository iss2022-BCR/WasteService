package it.unibo.radarSystem22.domainBCR.interfaces;

import it.unibo.radarSystem22.domainBCR.state.BuzzerState;

public interface IBuzzer extends IDevice {
    public void turnOn();
    public void turnOff();
    public void turnIntermittent();
    public BuzzerState getState();
}
