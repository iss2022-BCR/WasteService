import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandRunner {
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			System.out.println("ERROR: no command specified.");
			System.out.println("Usage:\n\tjava -jar commandRunner.jar \"command\" \"parameters\" [...]");
			return;
		}
		
		try {
			ProcessBuilder builder = new ProcessBuilder(args);
			builder.redirectErrorStream(true);
			Process process = builder.start();
		}
		catch(Exception e)
		{
			System.out.println("Usage: java -jar commandRunner.jar \"command\" \"parameters\" [...]");
			e.printStackTrace();
		}
	}
}