package it.unibo.radarSystem22.domainBCR.concrete;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import it.unibo.radarSystem22.domainBCR.Distance;
import it.unibo.radarSystem22.domainBCR.interfaces.ISonar;
import it.unibo.radarSystem22.domainBCR.models.SonarModel;
import it.unibo.radarSystem22.domainBCR.utils.ColorsOut;
import it.unibo.radarSystem22.domainBCR.utils.DomainSystemConfig;

public class SonarConcreteHCSR04 extends SonarModel implements ISonar {
	private Process process;
	private BufferedReader reader;

	/*
	 * curVal è usata come valore della distanza corrente misurata
	 */
	@Override
	protected void sonarSetUp()
	{
		curVal = new Distance();
	}
	
	@Override
	public void activate()
	{
		ColorsOut.out("[" + this.getClass().getSimpleName() + "] activate ");

		if(process == null)
		{
			String[] command = { "/usr/bin/python3", "-u", "./sonarBCR.py", "loop", "500"};
			ProcessBuilder builder = new ProcessBuilder(command);
			try {
				process = builder.start();
 				//p       = Runtime.getRuntime().exec("sudo ./SonarAlone");
 		        reader  = new BufferedReader( new InputStreamReader(process.getInputStream()));
 		        ColorsOut.out("[" + this.getClass().getSimpleName() + "] sonarSetUp done");
 	       	}catch( Exception e) {
 	       		ColorsOut.outerr("[" + this.getClass().getSimpleName() + "] sonarSetUp ERROR " + e.getMessage() );
 	    	}
 		}
 		super.activate();
 	}
	
	@Override
	protected void sonarProduce()
	{
        try {
			String data = reader.readLine();
			if(data == null) return;
			int v = Integer.parseInt(data);

			ColorsOut.out("[" + this.getClass().getSimpleName() + "]  v=" + v );

			int lastSonarVal = curVal.getVal();
			if(lastSonarVal != v && v < DomainSystemConfig.sonarDistanceMax)
			{
				//Eliminiamo dati del tipo 3430 //TODO: filtri in sottosistemi a stream
  	 			updateDistance(v);
			}
       } catch(Exception e) {
       		ColorsOut.outerr("[" + this.getClass().getSimpleName() + "] " + e.getMessage() );
       }		
	}
 
	@Override
	public void deactivate()
	{
		ColorsOut.out("[" + this.getClass().getSimpleName() + "] deactivate", ColorsOut.GREEN);

		curVal = new Distance();
		if(process != null)
		{
			process.destroy();  //Block the runtime process
			process = null;
		}
		super.deactivate();
 	}
}
