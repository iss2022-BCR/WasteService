package it.unibo.radarSystem22.domainBCR.interfaces;

public interface IRadarDisplay {
	public void update(String distance, String angle);
	public int getCurDistance(); //ADDED for testing
}
