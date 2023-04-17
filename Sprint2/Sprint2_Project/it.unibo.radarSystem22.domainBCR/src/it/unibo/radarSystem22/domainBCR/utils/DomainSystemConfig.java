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
	public static int DLIMT					=  30;
	public static boolean sonarObservable	=  false;
	public static int testingDistance		=  DLIMT - 2;

	// Led
	public static int ledDelay				=  100;
	public static boolean ledGui			= false;

	// Buzzer
	public static boolean enableBuzzer		= false;
	public static int buzzerDelay			=  100;
	public static boolean buzzerSound		= false;

	// TextDisplay
	public static boolean enableTextDisplay	= false;
	public static long textLine1Delay		= 1000;
	public static long textLine2Delay		= 1000;
	public static boolean textDisplay		= false;

	// Webcam
	public static boolean webcam			= false;
 

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

			sonarDelay			= object.getInt("sonarDelay");
			sonarDistanceMax	= object.getInt("sonarDistanceMax");
			sonarDistanceMin	= object.getInt("sonarDistanceMin");
			DLIMT				= object.getInt("DLIMT");
			//sonarObservable		= object.getBoolean("sonarObservable");
			//testingDistance		= object.getInt("testingDistance");

			ledDelay			= object.getInt("ledDelay");
			ledGui				= object.getBoolean("ledGui");

			enableBuzzer		= object.getBoolean("enableBuzzer");
			buzzerDelay			= object.getInt("buzzerDelay");
			buzzerSound			= object.getBoolean("buzzerSound");

			enableTextDisplay	= object.getBoolean("enableTextDisplay");
			textLine1Delay		= object.getLong("textLine1Delay");
			textLine2Delay		= object.getLong("textLine2Delay");
			textDisplay			= object.getBoolean("textDisplay");

			//webcam				= object.getBoolean("webCam");
	        
	        //tracing				= object.getBoolean("tracing");
	        //testing				= object.getBoolean("testing");

		} catch (FileNotFoundException e) {
 			ColorsOut.outerr("setTheConfiguration ERROR " + e.getMessage());
		}
	}
}
