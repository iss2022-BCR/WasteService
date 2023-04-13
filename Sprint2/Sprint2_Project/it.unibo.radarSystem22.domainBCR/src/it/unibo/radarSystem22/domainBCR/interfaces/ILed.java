package it.unibo.radarSystem22.domainBCR.interfaces;

import it.unibo.radarSystem22.domainBCR.utils.LedState;

public interface ILed extends IDevice {
	public void turnOn();
	public void turnOff();
	public void blink();
	public LedState getState();
}
