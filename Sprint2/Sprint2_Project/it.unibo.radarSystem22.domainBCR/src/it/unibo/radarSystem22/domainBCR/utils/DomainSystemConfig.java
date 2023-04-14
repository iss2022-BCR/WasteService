package it.unibo.radarSystem22.domainBCR.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.json.JSONObject;
import org.json.JSONTokener;

public class DomainSystemConfig {
	public static boolean simulation		= true;

	// Sonar
	public static int sonarDelay			=  500;
	public static int sonarDistanceMax		=  300;
	public static int sonarDistanceMin		=  5;
	public static boolean sonarObservable	=  false;
	public static int DLIMIT				=  30;
	public static int testingDistance		=  DLIMIT - 2;

	// Led
	public static boolean ledGui			= false;

	// Buzzer
	public static boolean buzzerSound		= false;

	// TextDisplay
	public static boolean textDisplay		= false;

	// WebCam
	public static boolean webCam			= false;
 

	public static boolean tracing			= false;
	public static boolean testing			= false;

	// TO-DO (BCR)
	/*public static String ledExecPath		= "./ledBCR.py";
	public static String sonarExecPath		= "./sonarBCR";
	public static String displayExecName	= "./displayBCR.py";
	public static String camExecPath		= "";*/


	public static void setTheConfiguration()
	{
		setTheConfiguration("../DomainSystemConfig.json");
	}
	
	public static void setTheConfiguration(String resourceName)
	{
		// Nella distribuzione resourceName � in una dir che include la bin
		FileInputStream fis = null;
		try {
			ColorsOut.out("%%% setTheConfiguration from file:" + resourceName);
			if(fis == null)
			{
 				 fis = new FileInputStream(new File(resourceName));
			}

	        JSONTokener tokenizer = new JSONTokener(fis);
	        JSONObject object   = new JSONObject(tokenizer);
	 		
	        simulation			= object.getBoolean("simulation");

	        webCam				= object.getBoolean("webCam");
	        
	        sonarObservable		= object.getBoolean("sonarObservable");
	        sonarDelay			= object.getInt("sonarDelay");
	        sonarDistanceMax	= object.getInt("sonarDistanceMax");
	        DLIMIT				= object.getInt("DLIMIT");
	        tracing				= object.getBoolean("tracing");
	        testing				= object.getBoolean("testing");
		} catch (FileNotFoundException e) {
 			ColorsOut.outerr("setTheConfiguration ERROR " + e.getMessage() );
		}
	}
}
