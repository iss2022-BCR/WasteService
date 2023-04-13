package it.unibo.radarSystem22.domainBCR.interfaces;

import java.util.Observer;

public interface IObserver extends Observer{
	public void update( String value );
	//From Observer: public void update(Observable o, Object news)
}
