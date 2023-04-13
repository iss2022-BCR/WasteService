package it.unibo.radarSystem22.domainBCR;

import it.unibo.radarSystem22.domainBCR.interfaces.IDistance;

public class Distance implements IDistance{
	public final static int DEFAULT_VALUE = -1;
	private int v;

	public Distance()
	{
		v = DEFAULT_VALUE;
	}

	public Distance(int d)
	{
		v = d;

		if(v < 0)
			v = -1;
	}

	public Distance(String d)
	{
		try {
			v = Integer.parseInt(d);
		}
		catch (NumberFormatException e) {
			v = DEFAULT_VALUE;
		}
	}

	@Override
	public int getVal()
	{
		return v;
	}
	    
	@Override
	public String toString()
	{
		return "" + v;
	}
}
